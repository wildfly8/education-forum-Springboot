package com.wst.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.wst.model.article.ArticleService;
import com.wst.model.category.CategoryService;
import com.wst.model.category.CategoryTO;
import com.wst.model.user.RoleTO;
import com.wst.model.user.UserService;
import com.wst.model.user.UserTO;
import com.wst.websecurity.UserValidator;


@RestController
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Value("${spring.mail.username}")
	private String smtpEmailUsername;
	
	@Autowired
    private JavaMailSender sender;
	
    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
	private CategoryService categoryService;
    
	@Autowired
	private ArticleService articleService;
	
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView userLogin(Model model, String error, String logout, RedirectAttributes redir) {
        if (error != null) {
            model.addAttribute("error", "Your username and password is invalid! (Have you clicked verification through our sent-out confirmation email after you registered?)");
        }
        if (logout != null) {
        	redir.addFlashAttribute("message", "You have been logged out successfully.");
            SecurityContextHolder.getContext().setAuthentication(null);
	        return new ModelAndView(new RedirectView("/", true));
        }
	    return new ModelAndView("jsp/login");
    }
	
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registration(Model model) {
    	Map<Integer, String> allCategoryMap = new LinkedHashMap<Integer, String>();
    	List<CategoryTO> allCategories = categoryService.getAllCategories();
    	for(CategoryTO category : allCategories) {
    		allCategoryMap.put(category.getId(), category.getName());
    	}
		model.addAttribute("allCategories", allCategoryMap);
        model.addAttribute("userForm", new UserTO());
        return new ModelAndView("jsp/registration");
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView registration(@ModelAttribute("userForm") UserTO userForm, BindingResult bindingResult, RedirectAttributes redir, HttpServletRequest request) throws Exception {
    	//default USER role
    	Set<RoleTO> roles = new HashSet<RoleTO>();
    	RoleTO role = new RoleTO();
    	role.setId((long) 2);
    	role.setName("ROLE_USER");					
    	roles.add(role);
    	userForm.setRoles(roles);
        Date now = Calendar.getInstance().getTime();
        userForm.setCreateDate(now);
        userForm.setLastUpdate(now);
    	//server-side input validation
        userValidator.validate(userForm, bindingResult);
        if (bindingResult.hasErrors()) {
    	    redir.addFlashAttribute("errors", bindingResult.getFieldErrors());
            return new ModelAndView(new RedirectView("/registration", true));
        }
        //convert MultipartFile to Blob & base64ImageString formats
        MultipartFile icon = userForm.getIcon();
        if (icon == null || icon.isEmpty()) { 
        	try {
        		userForm.setIconBlob(new javax.sql.rowset.serial.SerialBlob(IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/avatar-display.png"))));
        	} catch (IOException e) {
        		logger.error(e.getMessage());
        	} catch (SerialException e) {
        		logger.error(e.getMessage());
			} catch (SQLException e) {
        		logger.error(e.getMessage());
			}
        } else {
        	try {
        		userForm.setIconBlob(new javax.sql.rowset.serial.SerialBlob(icon.getBytes()));
        	} catch (IOException e) {
        		logger.error(e.getMessage());
        	} catch (SerialException e) {
        		logger.error(e.getMessage());
			} catch (SQLException e) {
        		logger.error(e.getMessage());
			}
        }
		//disable user until they click on confirmation link in email
        userForm.setEnabled(false);
	    //generate random 36-character string token for confirmation link
        userForm.setConfirmationToken(UUID.randomUUID().toString());
	    //save the unconfirmed new user to database temporarily
        userService.save(userForm);
        //send confirmation email
        sendEmail(userForm, request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/confirm?confirmationToken=" + userForm.getConfirmationToken());
        //securityService.autologin(userForm.getUsername(), userForm.getPasswordConfirm());
        return new ModelAndView(new RedirectView("forum", true));
    }
    
    @RequestMapping(value = "/profile-edit", method = RequestMethod.GET)
    public ModelAndView profileEdit(Model model, @RequestParam String username) {
    	UserTO user = userService.findByUsername(username);
    	if(user == null) {
    	    return new ModelAndView("jsp/login");
    	}
    	if(user.getIconBlob() != null) {
        	try {
                byte[] encodeBase64 = Base64.getEncoder().encode(user.getIconBlob().getBytes(1L, (int) user.getIconBlob().length()));
                user.setBase64imageString(new String(encodeBase64, "UTF-8"));	
        	} catch(UnsupportedEncodingException e) {
    			logger.error(e.getMessage());
        	} catch (SQLException e) {
    			logger.error(e.getMessage());
    		}	
    	}
        model.addAttribute("userForm", user);
		model.addAttribute("allCategories", categoryService.getAllCategories());
        return new ModelAndView("thymeleaf/user/profile-edit");
    }
    
    @RequestMapping(value = "/profile-edit", method = RequestMethod.POST)
    public ModelAndView profileEdit(@ModelAttribute("userForm") UserTO userForm, BindingResult bindingResult, RedirectAttributes redir) {
    	UserTO user = userService.findByUsername(userForm.getUsername());
    	user.setUpdate(true);
    	user.setEmail(userForm.getEmail());
    	user.setPasswordOld(user.getPassword());
    	user.setPasswordOldUI(userForm.getPasswordOldUI());
    	user.setPassword(userForm.getPassword());
    	user.setPasswordConfirm(userForm.getPasswordConfirm());
    	user.setUserCategories(userForm.getUserCategories());
    	user.setLastUpdate(Calendar.getInstance().getTime());
    	user.setSignature(userForm.getSignature());
    	if(userForm.getIcon() != null) {
    		user.setIcon(userForm.getIcon());
            //convert MultipartFile to byte[] & base64ImageString formats
            MultipartFile icon = user.getIcon();
            if (!icon.isEmpty() && icon != null) {                
            	try {
            		user.setIconBlob(new javax.sql.rowset.serial.SerialBlob(icon.getBytes()));
            	} catch (IOException e) {
            		logger.error(e.getMessage());
            	} catch (SerialException e) {
            		logger.error(e.getMessage());
				} catch (SQLException e) {
            		logger.error(e.getMessage());
				}
            }
    	}
    	//server-side input validation
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
    	    redir.addFlashAttribute("errors", bindingResult.getFieldErrors());
            return new ModelAndView(new RedirectView("/profile-edit?username=" + user.getUsername(), true));
        }
        userService.save(user);
        return new ModelAndView(new RedirectView("forum", true));
    }
    
    @RequestMapping(value = "/profile-view", method = RequestMethod.GET)
    public ModelAndView profileView(Model model, @RequestParam String username) {
    	UserTO user = userService.findByUsername(username);
    	if(user.getIconBlob() != null) {
        	try {
                byte[] encodeBase64 = Base64.getEncoder().encode(user.getIconBlob().getBytes(1L, (int) user.getIconBlob().length()));
                user.setBase64imageString(new String(encodeBase64, "UTF-8"));	
        	} catch(UnsupportedEncodingException e) {
    			logger.error(e.getMessage());
        	} catch (SQLException e) {
    			logger.error(e.getMessage());
    		}
    	}
        model.addAttribute("user", user);
		model.addAttribute("allCategories", categoryService.getAllCategories());
        return new ModelAndView("thymeleaf/user/profile-view");
    }
    
    @RequestMapping(value = "/confirm", method = {RequestMethod.GET, RequestMethod.PATCH})
    public ModelAndView confirmEmail(Model model, @RequestParam String confirmationToken) {
    	UserTO user = userService.findByConfirmationToken(confirmationToken);
    	if(user == null || user.getEnabled()) {
            model.addAttribute("user", user);
            return new ModelAndView("thymeleaf/user/confirmed-bad");
    	} else {
    		user.setEnabled(true);
    		user.setConfirmedDate(Calendar.getInstance().getTime());
            userService.updateEnabled(user.getId(), user.getEnabled(), user.getConfirmedDate());
            model.addAttribute("user", user);
            return new ModelAndView("thymeleaf/user/confirmed-ok");
    	}
    }
    
	@RequestMapping(value = "/users", method = RequestMethod.GET, produces = "application/json")
	public List<UserTO> getAllUsers() {
		List<UserTO> users = userService.getAllUsers();
		for(UserTO user : users) {
			user.setNumOfPosts(articleService.countByAuthor(user));
		}
		return users;
	}
    
    private void sendEmail(UserTO user, String emailConfirmUrl) throws Exception{
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(smtpEmailUsername);
        helper.setTo(user.getEmail());
        helper.setSubject("Welcome to Wisdom Spring Technologies! New User Registration Confirmation.");
        helper.setText("(This is a system auto-generated email, please do NOT reply!)" 
        		+ "\n\nDear " + user.getUsername() + "," + "\n\nWelcome to Wisdom Spring Technologies! After becoming a member, you can enjoy unlimited viewing and posting any questions on our courses forum."
        		+ "\n\nTo confirm your e-mail address, please click the link below:\n" + emailConfirmUrl 
        		+ "\n\n\nBest Regards,\n\nWisdom Spring Technologies");
        sender.send(message);
    }
    
}
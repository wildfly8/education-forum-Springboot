package com.wst.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.wst.model.category.CategoryService;
import com.wst.model.user.UserService;
import com.wst.websecurity.SecurityService;


@Controller
public class IndexController {
	
	@Autowired
    private SecurityService securityService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
	private CategoryService categoryService;
    
    @Autowired
    @Qualifier("sessionRegistry")
    private SessionRegistry sessionRegistry;
    

    @RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index() {  	
		Collection<? extends GrantedAuthority> auths = securityService.getLoggedInUserGrantedAuthorities();
        for(GrantedAuthority auth : auths) {
    	   if(auth.getAuthority().equalsIgnoreCase("ROLE_ADMIN")) {
    		   return new ModelAndView(new RedirectView("/stats", true));
    	   }
       }
       return new ModelAndView("thymeleaf/index");
	}
    
    @RequestMapping(value = {"/forum"}, method = RequestMethod.GET)
    public String forum(Model model) {
    	List<Object> principals = sessionRegistry.getAllPrincipals();
    	List<String> usersNamesList = new ArrayList<String>();
    	for (Object principal : principals) {
    	    if (principal instanceof User) {
    	    	List<SessionInformation> activeUserSessions = sessionRegistry.getAllSessions(principal, false);
    	    	if (activeUserSessions != null && !activeUserSessions.isEmpty()) {
        	        usersNamesList.add(((User) principal).getUsername());
                }
    	    }
    	}
		model.addAttribute("allLoggedInUsers", usersNamesList);
		model.addAttribute("allCategories", categoryService.getAllCategories());
		String username = securityService.findLoggedInUsername();
		if("anonymousUser".equals(username)) {
			model.addAttribute("role", "ROLE_ANONYMOUS");			
		} else {
			model.addAttribute("role", userService.findByUsername(username).getRoles().iterator().next().getName());
		}
        return "thymeleaf/forum";
    }
    
    @RequestMapping(value = "/fundraising", method = RequestMethod.GET)
    public String fundraising() {
        return "thymeleaf/fundraising";
    }
    
    @RequestMapping(value = "/terms", method = RequestMethod.GET)
    public String terms() {
        return "thymeleaf/terms";
    }
    
}
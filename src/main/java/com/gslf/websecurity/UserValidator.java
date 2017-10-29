package com.gslf.websecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.gslf.model.user.UserService;
import com.gslf.model.user.UserTO;


@Component
public class UserValidator implements Validator {
	
    @Value("${NotEmpty}")
    private String notEmpty = "";

    @Value("${Size.userForm.username}")
    private String sizeUserform = "";

    @Value("${Duplicate.userForm.username}")
    private String duplicateUserform = "";
    
    @Value("${Mismatch.userForm.oldpassword}")
    private String mismatchOldPassword = "";
    
    @Value("${Size.userForm.password}")
    private String sizePassword = "";
    
    @Value("${Diff.userForm.passwordConfirm}")
    private String diffPassword = "";
    
    @Value("${Size.userForm.email}")
    private String sizeEmail = "";
    
    @Value("${Size.userForm.role}")
    private String sizeRole = "";  
	
    @Autowired
    private UserService userService;

    
    @Override
    public boolean supports(Class<?> aClass) {
        return UserTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserTO user = (UserTO) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", notEmpty);
        if (user.getUsername().length() < 6 || user.getUsername().length() > 32) {
            errors.rejectValue("username", sizeUserform);
        }
        if(user.getUpdate()) {
        	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        	if(!passwordEncoder.matches(user.getPasswordOldUI(), user.getPasswordOld())) {
                errors.rejectValue("passwordOld", mismatchOldPassword);
        	}
        } else {
        	if(userService.findByUsername(user.getUsername()) != null) {
                errors.rejectValue("username", duplicateUserform);
        	}
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", notEmpty);
        if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            errors.rejectValue("password", sizePassword);
        }
        if (!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", diffPassword);
        }
        if(user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().endsWith(".com")) {
            errors.rejectValue("email", sizeEmail);
        }
        if(user.getRoles() == null || user.getRoles().isEmpty()) {
            errors.rejectValue("roles", sizeRole);
        }
        if(!user.getUpdate()) {
            if(user.getIcon() == null) {
                errors.rejectValue("icon", "Please select an image file to upload!");
            } else if(!(user.getIcon().getContentType().equals("application/octet-stream") ||
            			user.getIcon().getContentType().equals("image/jpeg") ||
            			user.getIcon().getContentType().equals("image/png") ||
            			user.getIcon().getContentType().equals("image/gif"))) {
                errors.rejectValue("icon", "Please use only image-type file to upload!");
            }
        }
    }
    
}
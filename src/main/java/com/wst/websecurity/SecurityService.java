package com.wst.websecurity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public interface SecurityService {
	
    String findLoggedInUsername();
    
    Collection<? extends GrantedAuthority> getLoggedInUserGrantedAuthorities();

    void autologin(String username, String password);
    
}
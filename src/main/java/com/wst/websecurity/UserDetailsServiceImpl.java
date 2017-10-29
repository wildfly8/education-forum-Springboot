package com.wst.websecurity;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wst.model.user.RoleTO;
import com.wst.model.user.UserRepository;
import com.wst.model.user.UserTO;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
    @Autowired
    private UserRepository userRepository;

    
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserTO user = userRepository.findByUsername(username);
        if(user == null) {
        	throw new UsernameNotFoundException("Username " + username + " Not Found!");
        }
        if(!user.getEnabled()) {
        	throw new UsernameNotFoundException("Username " + username + " Not Verified!");
        }
        Set<RoleTO> roles = user.getRoles();
        if(roles == null || roles.isEmpty()) {
        	throw new UsernameNotFoundException("Username " + username + " No Role is Found!");
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
        for(RoleTO role : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
    
}

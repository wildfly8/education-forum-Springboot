package com.gslf.model.user;

import java.util.Date;
import java.util.List;


public interface UserService {
	
	public List<UserTO> getAllUsers();
	
    void save(UserTO user);
    
	int updateEnabled(Long id, Boolean enabled, Date confirmedDate);

    UserTO findByUsername(String username);
    
    UserTO findByConfirmationToken(String confirmationToken);
    
}
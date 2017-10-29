package com.gslf.model.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
	
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    
	public List<UserTO> getAllUsers() {
		List<UserTO> users = new ArrayList<UserTO>();
		userRepository.findAll().forEach(users::add);
		return users;
	}
	
    @Override
    public void save(UserTO user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
    
    @Override
    public int updateEnabled(Long id, Boolean enabled, Date confirmedDate) {
        return userRepository.updateEnabled(id, enabled, confirmedDate);
    }
    
    @Override
    public UserTO findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

	@Override
	public UserTO findByConfirmationToken(String confirmationToken) {
		for(UserTO user : userRepository.findAll()) {
			if(confirmationToken.equals(user.getConfirmationToken())) {
				return user;
			}
		}
		return null;
	}
    
}
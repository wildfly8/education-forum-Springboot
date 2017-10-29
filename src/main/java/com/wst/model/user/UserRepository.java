package com.wst.model.user;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Transactional
public interface UserRepository extends JpaRepository<UserTO, Long> {
	
    UserTO findByUsername(String username);
    
    @Modifying
    @Query("UPDATE UserTO u SET u.enabled = :enabled, u.confirmedDate = :confirmedDate WHERE u.id = :id")
    int updateEnabled(@Param("id") Long id, @Param("enabled") Boolean enabled, @Param("confirmedDate") Date confirmedDate);
    
}
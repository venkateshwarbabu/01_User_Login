package com.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.entity.UserInfo;

public interface UserRepo extends JpaRepository<UserInfo,Integer> {
	
	public UserInfo findByUserEmail(String userEmail);

}

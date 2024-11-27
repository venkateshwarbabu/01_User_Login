package com.user.service;

import java.util.List;

import com.user.binding.ActivateAccount;
import com.user.binding.LoginUser;
import com.user.binding.UserRegister;
import com.user.binding.ViewAccounts;

public interface UserService {
	
	public boolean registerUser(UserRegister userRegister);
	
	public boolean activateAcc(ActivateAccount activateAccount);
	
	public String logIn(LoginUser loginUser);
	
	public List<ViewAccounts> getAllUsers();
	
	public String forgetPwd(String email);
	
	public boolean deleteUserById(Integer userId);
	
	public UserRegister getUserById(Integer userId);
	
	public boolean userAccountStatus(Integer userId,String accStatus);
	
	

}

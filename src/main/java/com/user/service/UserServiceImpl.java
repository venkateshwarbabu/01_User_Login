package com.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.user.binding.ActivateAccount;
import com.user.binding.LoginUser;
import com.user.binding.UserRegister;
import com.user.binding.ViewAccounts;
import com.user.entity.UserInfo;
import com.user.repository.UserRepo;
import com.user.utils.EmailUtils;

@Service
public class UserServiceImpl implements UserService{
	
	private UserRepo userRepo;
	
	private EmailUtils emailUtils;
	

	public UserServiceImpl(UserRepo userRepo,EmailUtils emailUtils) {
		this.userRepo = userRepo;
		this.emailUtils=emailUtils;
	}
	
	private Random random = new Random();

	@Override
	public boolean registerUser(UserRegister userRegister) {
	UserInfo  entity= new UserInfo();
	UserInfo user1 = userRepo.findByUserEmail(userRegister.getUserEmail());
	if(user1==null) {
		
		BeanUtils.copyProperties(userRegister, entity);
		String tempPwd = generateTempPwd();
		entity.setPassword(tempPwd);
		
		entity.setActiveStatus("In-Active");
		UserInfo save = userRepo.save(entity);
		
		String url="http://13.126.117.5:8080/users/status/"+save.getUserId()+"/Active";
		
		
		String mailBody = emailUtils.createMailBody(userRegister.getUserName(),"temporary passwor is: "+save.getPassword(), url, "active your account :");
//		String subject = "Your Registration is Success";
//		String body="the password will be Generated :" + tempPwd;
		
		emailUtils.sendMail(userRegister.getUserEmail(), "Registration Successfull",mailBody);
	

		
			return true;
		}   
	
		return false;
		
	}

	@Override
	public boolean activateAcc(ActivateAccount activateAccount) {
		UserInfo entity = new UserInfo();
		entity.setUserEmail(activateAccount.getEmail());
		entity.setPassword(activateAccount.getTemPassword());
		Example<UserInfo> example = Example.of(entity);
		List<UserInfo> findAll = userRepo.findAll(example);
		if (findAll.isEmpty()) {
			return false;
		} else {
			UserInfo userEntity = findAll.get(0);
			userEntity.setPassword(activateAccount.getNewPwd());
			userEntity.setActiveStatus("Active");
			userRepo.save(userEntity);
			return true;
		}
	}

	@Override
	public String logIn(LoginUser loginUser) {
		UserInfo entity = new UserInfo();
		entity.setUserEmail(loginUser.getUserEmail());
		entity.setPassword(loginUser.getPassword());
		Example<UserInfo> example = Example.of(entity);
		List<UserInfo> findAll = userRepo.findAll(example);
		if (findAll.isEmpty()) {
			return "Invalid Credentials";
		} else {
			UserInfo userEntity= findAll.get(0);
			if (userEntity.getActiveStatus().equals("Active")) {
				return "Login Success !";
			} else {
				return "Account not activated. Please activate account";
			}
		}
			
	}
	
	@Override
	public String forgetPwd(String email) {
		UserInfo entity = userRepo.findByUserEmail(email);
		if(entity==null) {
			return "INVALID-EMAIL";
		}

		String subject="Forget Password";
		String body="your password:: " +entity.getPassword();
		boolean isSent= emailUtils.sendMail(email, subject, body);
		if(isSent) {
			return "Password sent to your  registered email.";
		}
		return "please check is your accout activae or not.";
	}


	@Override
	public List<ViewAccounts> getAllUsers() {
		List<ViewAccounts> response = new ArrayList<>();
		List<UserInfo> findAll = userRepo.findAll();
		for (UserInfo entity : findAll) {
			ViewAccounts user = new ViewAccounts();
			BeanUtils.copyProperties(entity, user);
			response.add(user);
		}
		return response;
	}

	

	@Override
	public boolean deleteUserById(Integer userId) {
		Optional<UserInfo> byId = userRepo.findById(userId);
		if(byId.isPresent()) {
			userRepo.deleteById(userId);
			 return true;
		}
		
		return false;
	}

	@Override
	public UserRegister getUserById(Integer userId) {
		Optional<UserInfo> findById = userRepo.findById(userId);
		if (findById.isPresent()) {
			UserRegister response = new UserRegister();
			UserInfo user = findById.get();
			BeanUtils.copyProperties(user,response);
			return response;
		}
		return null;
	}

	@Override
	public boolean userAccountStatus(Integer userId, String accStatus) {
		Optional<UserInfo> findById = userRepo.findById(userId);
		if (findById.isPresent()) {
			UserInfo userEntity = findById.get();
			userEntity.setActiveStatus(accStatus);
			userRepo.save(userEntity);
			return true;
		}
		return false;
	}

	
	private String generateTempPwd() {

		String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "0123456789";
		
		String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;
		StringBuilder sb = new StringBuilder();
		
		int length = 6;
		
		for (int i = 0; i < length; i++) {
			
			int index = this.random.nextInt(alphaNumeric.length());
			char randomChar = alphaNumeric.charAt(index);
			sb.append(randomChar);
		}
		String randomString = sb.toString();
		//System.out.println("Random String is: " + randomString);
		return randomString;
	}

	
}

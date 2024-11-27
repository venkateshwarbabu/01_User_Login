package com.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.binding.ActivateAccount;
import com.user.binding.LoginUser;
import com.user.binding.UserRegister;
import com.user.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/sign-up")
	public ResponseEntity<String> userReg(@RequestBody UserRegister userRegister) {
		boolean registerUser = userService.registerUser(userRegister);
		if (registerUser) {
			return new ResponseEntity<String>("Registration Success :: password will be sent to your email ", HttpStatus.CREATED);
		} else{
			return new ResponseEntity<String>("Registration Failed:: enter valid email", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/activate")
	public ResponseEntity<String> activateAcc(@RequestBody ActivateAccount activateAccount) {
		boolean activateAcc = userService.activateAcc(activateAccount);
		if (activateAcc) {
			return new ResponseEntity<>("Account activated", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Invalid Temporary Password", HttpStatus.BAD_REQUEST);
		}

	}

	@DeleteMapping("/user/{userId}")
	public ResponseEntity<String> deleteUserById(@PathVariable Integer userId) {
		boolean isDeleted = userService.deleteUserById(userId);
		if (isDeleted) {
			return new ResponseEntity<String>("User Deleted successfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("User Delete Failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/status/{userId}/{accStatus}")
	public ResponseEntity<String> statusChange(@PathVariable Integer userId, @PathVariable String accStatus) {
		boolean isChanged = userService.userAccountStatus(userId, accStatus);
		if (isChanged) {
			return new ResponseEntity<>("Status Changed", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Failed to Change", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/loginUser")
	public ResponseEntity<String> login(@RequestBody LoginUser loginUser) {
		String logIn = userService.logIn(loginUser);
		return new ResponseEntity<String>(logIn, HttpStatus.OK);

		
	}

	@GetMapping("/forgotpwd/{userEmail}")
	public ResponseEntity<String> forgotPwd(@PathVariable String userEmail) {
		String forgetPwd = userService.forgetPwd(userEmail);
		return new ResponseEntity<String>(forgetPwd, HttpStatus.OK);
	}

}

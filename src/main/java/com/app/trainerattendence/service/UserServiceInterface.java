package com.app.trainerattendence.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.app.trainerattendence.model.User;

public interface UserServiceInterface {

	String registerUser(User user);

	List<User> getAllNormalUsers();

	List<User> getAllUsers();

	User getUserByEmail(String email);

	User getUserByUserId(String userId);

	Object loginUser(String email, String password);

	User uploadProfilePhoto(String userId, MultipartFile file) throws IOException;

	byte[] getProfilePhoto(String userId);

	Object changePassword(String userId, String oldPassword, String newPassword);

	Object updateName(String userId, String newName);

}

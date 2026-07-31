package com.talentbridge.service.impl;

import org.springframework.stereotype.Service;

import com.talentbridge.entity.User;
import com.talentbridge.repository.UserRepository;
import com.talentbridge.service.UserProfileService;

@Service
public class UserProfileServiceImpl implements UserProfileService {

	private final UserRepository userRepository;

	public UserProfileServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User getProfile(String email) {

		return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
	}

	@Override
	public User updateProfile(String email, User updatedUser) {

		User existingUser = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		existingUser.setFullName(updatedUser.getFullName());

		return userRepository.save(existingUser);
	}

}
package com.talentbridge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.talentbridge.entity.User;
import com.talentbridge.service.UserProfileService;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

	private final UserProfileService userProfileService;

	public UserProfileController(UserProfileService userProfileService) {

		this.userProfileService = userProfileService;
	}

	@GetMapping("/profile")
	public ResponseEntity<User> getProfile(Authentication authentication) {

		String email = authentication.getName();

		return ResponseEntity.ok(userProfileService.getProfile(email));
	}

	@PutMapping("/profile")
	public ResponseEntity<User> updateProfile(Authentication authentication, @RequestBody User user) {

		String email = authentication.getName();

		return ResponseEntity.ok(userProfileService.updateProfile(email, user));
	}

}
package com.talentbridge.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.talentbridge.dto.request.LoginRequest;
import com.talentbridge.dto.request.RegistrationRequest;
import com.talentbridge.dto.response.AuthenticationResponse;
import com.talentbridge.entity.User;
import com.talentbridge.enums.Role;
import com.talentbridge.repository.UserRepository;
import com.talentbridge.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {

		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public AuthenticationResponse register(RegistrationRequest request) {

		if (userRepository.existsByEmail(request.getEmail())) {

			throw new RuntimeException("Email already registered");
		}

		User user = new User();

		user.setFullName(request.getFullName());
		user.setEmail(request.getEmail());

		user.setPassword(passwordEncoder.encode(request.getPassword()));

		user.setRole(request.getRole() != null ? request.getRole() : Role.CANDIDATE);

		userRepository.save(user);

		return new AuthenticationResponse(null, user.getEmail(), user.getRole());
	}

	@Override
	public AuthenticationResponse login(LoginRequest request) {

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("Invalid email or password"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {

			throw new RuntimeException("Invalid email or password");
		}

		return new AuthenticationResponse(null, user.getEmail(), user.getRole());
	}

}
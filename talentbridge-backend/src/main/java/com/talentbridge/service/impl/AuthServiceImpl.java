package com.talentbridge.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.talentbridge.dto.request.LoginRequest;
import com.talentbridge.dto.request.RegistrationRequest;
import com.talentbridge.dto.response.AuthenticationResponse;
import com.talentbridge.entity.User;
import com.talentbridge.enums.Role;
import com.talentbridge.repository.UserRepository;
import com.talentbridge.security.JwtService;
import com.talentbridge.service.AuthService;
import com.talentbridge.exception.InactiveUserException;

@Service
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {

		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
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

		user.setRole(Role.CANDIDATE);

		userRepository.save(user);

		return new AuthenticationResponse(jwtService.generateToken(user.getEmail()), user.getEmail(), user.getRole());
	}

	@Override
	public AuthenticationResponse login(LoginRequest request) {

		System.out.println("====== LOGIN START ======");
		System.out.println("EMAIL RECEIVED = " + request.getEmail());
		System.out.println("PASSWORD RECEIVED = " + request.getPassword());

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("Invalid email or password"));

		System.out.println("USER FOUND = " + user.getEmail());
		System.out.println("DB PASSWORD = " + user.getPassword());
		System.out.println("ACTIVE = " + user.isActive());
		System.out.println("ROLE = " + user.getRole());

		boolean match = passwordEncoder.matches(request.getPassword(), user.getPassword());

		System.out.println("PASSWORD MATCH = " + match);

		if (!match) {
			throw new RuntimeException("Invalid email or password");
		}

		if (!user.isActive()) {
			System.out.println("USER IS INACTIVE");
			throw new InactiveUserException("User account is inactive");
		}

		System.out.println("LOGIN SUCCESS");

		return new AuthenticationResponse(jwtService.generateToken(user.getEmail()), user.getEmail(), user.getRole());
	}

}
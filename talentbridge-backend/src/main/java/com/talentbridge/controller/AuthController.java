package com.talentbridge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.talentbridge.dto.request.LoginRequest;
import com.talentbridge.dto.request.RegistrationRequest;
import com.talentbridge.dto.response.AuthenticationResponse;
import com.talentbridge.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationRequest request) {

		return ResponseEntity.ok(authService.register(request));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {

		return ResponseEntity.ok(authService.login(request));
	}

}
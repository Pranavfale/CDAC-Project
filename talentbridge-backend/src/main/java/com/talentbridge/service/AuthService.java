package com.talentbridge.service;

import com.talentbridge.dto.request.LoginRequest;
import com.talentbridge.dto.request.RegistrationRequest;
import com.talentbridge.dto.response.AuthenticationResponse;

public interface AuthService {

	AuthenticationResponse register(RegistrationRequest request);

	AuthenticationResponse login(LoginRequest request);

}
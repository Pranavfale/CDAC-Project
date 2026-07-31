package com.talentbridge.dto.response;

import com.talentbridge.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationResponse {

	private String token;

	private String email;

	private Role role;

}
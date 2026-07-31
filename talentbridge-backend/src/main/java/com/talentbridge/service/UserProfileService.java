package com.talentbridge.service;

import com.talentbridge.entity.User;

public interface UserProfileService {

	User getProfile(String email);

	User updateProfile(String email, User user);

}
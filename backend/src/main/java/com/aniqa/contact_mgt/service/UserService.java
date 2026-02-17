package com.aniqa.contact_mgt.service;


import com.aniqa.contact_mgt.dto.UserLoginRequest;
import com.aniqa.contact_mgt.dto.UserRegistrationRequest;
import com.aniqa.contact_mgt.dto.UserResponse;

public interface UserService {

    UserResponse register(UserRegistrationRequest request);

    UserResponse login(UserLoginRequest request);

    void changePassword(String userId, String oldPassword, String newPassword);

    UserResponse getUserProfile(String userId);

}

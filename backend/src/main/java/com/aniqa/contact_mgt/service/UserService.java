package com.aniqa.contact_mgt.service;

import com.aniqa.contact_mgt.dto.UserRegistrationLoginRequest;
import com.aniqa.contact_mgt.dto.UserResponse;

public interface UserService {

    UserResponse register(UserRegistrationLoginRequest request);

    UserResponse login(UserRegistrationLoginRequest request);

    void changePassword(String userId, String oldPassword, String newPassword);

}

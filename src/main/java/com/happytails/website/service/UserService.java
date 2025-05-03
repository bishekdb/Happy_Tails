package com.happytails.website.service;

import com.happytails.website.dto.UserRegistrationDto;
import com.happytails.website.model.User;

public interface UserService {
    User registerNewUser(UserRegistrationDto registrationDto);
    User findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
} 
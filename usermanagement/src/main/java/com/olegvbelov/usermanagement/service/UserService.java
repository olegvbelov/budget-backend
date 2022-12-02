package com.olegvbelov.usermanagement.service;

import com.olegvbelov.usermanagement.dto.UserDto;

public interface UserService {
    UserDto getUserById(String id);
    
    UserDto createOrUpdate(UserDto userDto);
}

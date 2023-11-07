package com.vicgan.todoapi.services;

import com.vicgan.todoapi.dtos.UpdateUserProfileDto;
import com.vicgan.todoapi.dtos.UserDto;
import com.vicgan.todoapi.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface UserService {

    UserDto getUserById(String id);

    List<UserDto> getAllUsers(Specification<User> userSpec, org.springframework.data.domain.Pageable pageable);

    UserDto updateUserProfile(UpdateUserProfileDto updateUserProfileDto);

    void deleteUserProfile(String id);

}

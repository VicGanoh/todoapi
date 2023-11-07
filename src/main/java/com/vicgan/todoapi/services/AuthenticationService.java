package com.vicgan.todoapi.services;

import com.vicgan.todoapi.dtos.SignUpDto;
import com.vicgan.todoapi.dtos.SignInDto;
import com.vicgan.todoapi.dtos.UserDto;
import com.vicgan.todoapi.response.ApiResponse;
import com.vicgan.todoapi.response.AuthenticationResponse;

public interface AuthenticationService {

    UserDto registerUser(SignUpDto signUpDto);

    AuthenticationResponse signIn(SignInDto signInDto);
}

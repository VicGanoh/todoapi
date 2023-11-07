package com.vicgan.todoapi.controller.v1;

import com.vicgan.todoapi.dtos.SignUpDto;
import com.vicgan.todoapi.dtos.SignInDto;
import com.vicgan.todoapi.dtos.UserDto;
import com.vicgan.todoapi.entities.User;
import com.vicgan.todoapi.repositories.UserRepository;
import com.vicgan.todoapi.response.ApiResponse;
import com.vicgan.todoapi.response.AuthenticationResponse;
import com.vicgan.todoapi.services.impl.AuthenticationServiceImpl;
import com.vicgan.todoapi.utils.JwtProvider;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationServiceImpl authService;

    @PostMapping("/signup")
    public ApiResponse<UserDto> signup(@Valid @RequestBody SignUpDto signUpDto){
        logger.info("HTTP Request: signUp() inside AuthController....");
        UserDto userDto = authService.registerUser(signUpDto);

        ApiResponse<UserDto> response = new ApiResponse<>(userDto, HttpStatusCode.valueOf(HttpStatus.CREATED.value()));
        logger.info("HTTP Response: signUp(): {}", response.getBody());
        return response;
    }

    @PostMapping("/authenticate")
    public ApiResponse<AuthenticationResponse> signIn(@Valid @RequestBody SignInDto signInDto){
        logger.info("HTTP Request: signIn() inside AuthController....");
        AuthenticationResponse authResponse = authService.signIn(signInDto);

        logger.info("HTTP Response: signIn....");
        return new ApiResponse<>(authResponse, HttpStatus.OK);
    }
}

package com.vicgan.todoapi.services.impl;

import com.vicgan.todoapi.dtos.SignUpDto;
import com.vicgan.todoapi.dtos.SignInDto;
import com.vicgan.todoapi.dtos.UserDto;
import com.vicgan.todoapi.entities.Role;
import com.vicgan.todoapi.entities.User;
import com.vicgan.todoapi.exception.AlreadyExistsException;
import com.vicgan.todoapi.repositories.UserRepository;
import com.vicgan.todoapi.response.ApiResponse;
import com.vicgan.todoapi.response.AuthenticationResponse;
import com.vicgan.todoapi.services.AuthenticationService;
import com.vicgan.todoapi.utils.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AuthenticationManager authenticationManager;


    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Override
    @Transactional
    public UserDto registerUser(SignUpDto signUpDto) {
        Optional<User> userOptional = userRepository.findByEmail(signUpDto.getEmail());

        if (userOptional.isPresent()){
            throw new AlreadyExistsException("Email already exists.");
        } else {
            User user = new User();
            user.setFirstName(signUpDto.getFirstName());
            user.setLastName(signUpDto.getLastName());
            user.setEmail(signUpDto.getEmail());
            user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
            user.setRoles(List.of(Role.USER));
            user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            user.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

            userRepository.save(user);



            String token = jwtProvider.generateToken(user);
            AuthenticationResponse authResponse = new AuthenticationResponse();
            authResponse.setToken(token);

            logger.info("Invoking registerUser() method inside AuthenticationServiceImpl...");
            UserDto userDto = mapper.map(user, UserDto.class);
            return userDto;
        }
    }

    @Override
    @Transactional
    public AuthenticationResponse signIn(SignInDto signInDto) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInDto.getEmail(), signInDto.getPassword()));

            Optional<User> userOptional = userRepository.findByEmail(signInDto.getEmail());

            if (userOptional.isPresent()){
                User user = userOptional.get();

                String token = jwtProvider.generateToken(user);

                AuthenticationResponse authResponse = new AuthenticationResponse();
                authResponse.setToken(token);

                logger.info("Invoking signIn() method inside AuthenticationServiceImpl...");
                return authResponse;
            } else {
                logger.error("Invalid username or password inside signIn() method... in AuthenticationServiceImpl...");
                throw new BadCredentialsException("Bad credentials");
            }
        }catch (BadCredentialsException ex){
            logger.error("Invalid username or password inside signIn() method... in AuthenticationServiceImpl...");
            throw new BadCredentialsException("Bad credentials");
        }
    }
}

package com.vicgan.todoapi.services.impl;

import com.vicgan.todoapi.dtos.UpdateUserProfileDto;
import com.vicgan.todoapi.dtos.UserDto;
import com.vicgan.todoapi.entities.User;
import com.vicgan.todoapi.exception.AlreadyExistsException;
import com.vicgan.todoapi.exception.NotFoundException;
import com.vicgan.todoapi.repositories.ImageRepository;
import com.vicgan.todoapi.repositories.UserRepository;
import com.vicgan.todoapi.services.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }

    @Override
    @Transactional
    public UserDto getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found."));

        logger.info("Invoking getUserById() method inside UserServiceImpl...");
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public List<UserDto> getAllUsers(Specification<User> userSpec, Pageable pageable) {
        Page<User> userPage = userRepository.findAll(userSpec, pageable);

        logger.info("Invoking getAllUsers() method inside UserServiceImpl...");
        return userPage.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public UserDto updateUserProfile(UpdateUserProfileDto updateUserProfileDto) {
        Optional<User> userOptional = userRepository.findById(updateUserProfileDto.getId());
        User updateUser = new User();

        if (userOptional.isPresent()){
            updateUser = userOptional.get();
            updateUser.setId(updateUser.getId());
            updateUser.setFirstName(updateUserProfileDto.getFirstName());
            updateUser.setLastName(updateUserProfileDto.getLastName());

            userRepository.save(updateUser);

            logger.info("Invoking updateUserProfile() method inside UserServiceImpl...");
            return modelMapper.map(updateUser, UserDto.class);
        } else {
            throw new NotFoundException("User not found");
        }
    }

    @Override
    public void deleteUserProfile(String id) {
    }
}

package com.vicgan.todoapi.controller.v1;

import com.vicgan.todoapi.dtos.ImageDto;
import com.vicgan.todoapi.dtos.UpdateUserProfileDto;
import com.vicgan.todoapi.dtos.UserDto;
import com.vicgan.todoapi.queryspec.ListUserSpec;
import com.vicgan.todoapi.response.ApiResponse;
import com.vicgan.todoapi.response.ImageResponse;
import com.vicgan.todoapi.services.impl.ImageServiceImpl;
import com.vicgan.todoapi.services.impl.UserServiceImpl;
import com.vicgan.todoapi.utils.IsAdmin;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ImageServiceImpl imageService;

    @GetMapping("/all")
    @IsAdmin
    public ApiResponse<List<UserDto>>  getAllUsers(ListUserSpec userSpec, Pageable pageable){
        try{
            logger.info("HTTP Request: getAllUsers inside UserController...");
            pageable = PageRequest.of(0, 5);
            List<UserDto> dtoResponse = userService.getAllUsers(userSpec, pageable);

            logger.info("HTTP Response: getAllUsers: {}", dtoResponse);
            return new ApiResponse<>(dtoResponse, HttpStatus.OK);
        }catch (AccessDeniedException e) {
            throw new AccessDeniedException("You don't have permission to access this resource");
        }
    }

    @GetMapping(path = "/{id}")
    @IsAdmin
    public ApiResponse<UserDto> getUserById(@Valid @PathVariable String id){
        logger.info("HTTP Request: getUserById: {}", id);
        UserDto dtoResponse = userService.getUserById(id);

        logger.info("HTTP response: {}", dtoResponse);
       return new ApiResponse<>(dtoResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/image/{name}")
    public ApiResponse<InputStreamResource> getImage(@PathVariable("name") String name){
        logger.info("HTTP Request: getImage(): {}", name);
        ImageDto image = imageService.getImage(name);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(image.getType()));
        headers.setContentDispositionFormData("attachment", image.getName());

        logger.info("HTTP Response: getImage(): {}", image.getName());
        return new ApiResponse<>(new InputStreamResource(new ByteArrayInputStream(image.getImage())), headers, HttpStatus.OK);
    }

    @PostMapping(path = "/upload/image")
    public ApiResponse<ImageResponse> uploadImage(@Valid @RequestPart(name = "image") MultipartFile file, Authentication authentication) throws IOException {
        String userEmail = authentication.getName();

        logger.info("HTTP Request: uploadImage(): {}", file);
        ImageResponse resourceFile = imageService.uploadImage(file, userEmail);
        return new ApiResponse<>(resourceFile, HttpStatus.CREATED);
    }


    @PutMapping
    public ApiResponse<UserDto> updateUser(@Valid @RequestBody UpdateUserProfileDto updateUserProfileDto){
        logger.info("HTTP Request: updateUser: {}", updateUserProfileDto);
        UserDto dtoResponse = userService.updateUserProfile(updateUserProfileDto);

        logger.info("HTTP Response: {}", dtoResponse);
        return new ApiResponse<>(dtoResponse, HttpStatus.OK);
    }

    @DeleteMapping(path = "/image/{id}")
    public ApiResponse<?> deleteImage(@PathVariable String id){
        imageService.deleteImageById(id);
        return new ApiResponse<>(HttpStatus.OK);
    }
}

package com.vicgan.todoapi.services.impl;

import com.vicgan.todoapi.dtos.ImageDto;
import com.vicgan.todoapi.entities.Image;
import com.vicgan.todoapi.entities.User;
import com.vicgan.todoapi.exception.AlreadyExistsException;
import com.vicgan.todoapi.exception.NotFoundException;
import com.vicgan.todoapi.repositories.ImageRepository;
import com.vicgan.todoapi.repositories.UserRepository;
import com.vicgan.todoapi.response.ImageResponse;
import com.vicgan.todoapi.services.ImageService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public ImageResponse uploadImage(MultipartFile file, String userEmail) throws IOException {

        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(file.getBytes());
            Image image = new Image();
            image.setName(file.getOriginalFilename());
            image.setType(file.getContentType());
            image.setImage(outputStream.toByteArray());
            image.setUser(user);
            imageRepository.save(image);

            modelMapper.map(image, ImageDto.class);

            logger.info("Invoking uploadImage() method inside ImageServiceImpl...");
            return new ImageResponse("Image saved successfully " + file.getOriginalFilename());
        }else {
            throw new NotFoundException("User not found");
        }
    }

    @Override
    @Transactional
    public ImageDto getImage(String name) {
        Optional<Image> imageOptional = imageRepository.findByName(name);
        if (imageOptional.isPresent()){
            Image image = imageOptional.get();

            logger.info("Invoking getImage() method inside ImageServiceImpl...");
            return modelMapper.map(image, ImageDto.class);
        } else {
            throw new NotFoundException("Image not found.");
        }
    }

    @Override
    public void deleteImageById(String id) {
       imageRepository.deleteById(id);
    }
}

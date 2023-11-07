package com.vicgan.todoapi.services;

import com.vicgan.todoapi.dtos.DeleteImageDto;
import com.vicgan.todoapi.dtos.ImageDto;
import com.vicgan.todoapi.repositories.ImageRepository;
import com.vicgan.todoapi.response.ImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    ImageResponse uploadImage(MultipartFile file, String email) throws IOException;

    ImageDto getImage(String name);

    void deleteImageById(String id);
}

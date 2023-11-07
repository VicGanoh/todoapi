package com.vicgan.todoapi.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class ImageDto implements Serializable {

    private String id;

    private String name;

    private String type;

    private byte[] image;
}

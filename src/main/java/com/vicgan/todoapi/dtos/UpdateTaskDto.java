package com.vicgan.todoapi.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateTaskDto implements Serializable {

    private String id;

    private String description;

    private boolean completed;
}

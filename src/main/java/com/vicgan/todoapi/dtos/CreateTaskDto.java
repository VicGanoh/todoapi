package com.vicgan.todoapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CreateTaskDto implements Serializable {

    @NotEmpty
    @NotBlank
    private String description;

    private boolean completed = false;
}

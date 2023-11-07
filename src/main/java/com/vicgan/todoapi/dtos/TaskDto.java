package com.vicgan.todoapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class TaskDto implements Serializable {

    @NotBlank
    @NotEmpty
    private String id;

    @NotBlank
    @NotEmpty
    private String description;

    private boolean completed;

    private String createdAt;

    private String updatedAt;

}

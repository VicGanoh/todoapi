package com.vicgan.todoapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {

    private String id;

    private String fullName;

    private String createdAt;

    private String updatedAt;
}

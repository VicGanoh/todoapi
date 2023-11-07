package com.vicgan.todoapi.dtos;

import java.io.Serial;
import java.io.Serializable;

public class DeleteImageDto implements Serializable {

    private String name;

    public DeleteImageDto(String name) {
        this.name = name;
    }

    public DeleteImageDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

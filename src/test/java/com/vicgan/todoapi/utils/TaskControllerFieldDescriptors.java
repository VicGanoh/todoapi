package com.vicgan.todoapi.utils;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class TaskControllerFieldDescriptors {

    public static final FieldDescriptor[] taskApiResponseFd = new FieldDescriptor[]{
            fieldWithPath("id").description("The task id").type(STRING),
            fieldWithPath("description").description("task description").type(JsonFieldType.STRING),
            fieldWithPath("completed").description("The completed status of the task"),
            fieldWithPath("created_at").description("Creation date and time"),
            fieldWithPath("updated_at").description("Date and time when the task was updated")
    };

    public static final FieldDescriptor[] listOfTaskApiResponseFd = new FieldDescriptor[]{
            fieldWithPath("[].id").description("The id of the task").type(STRING),
            fieldWithPath("[].description").description("task description").type(STRING),
            fieldWithPath("[].completed").description("Task completed or not").type(BOOLEAN),
            fieldWithPath("[].created_at").description("Date and time when the task was created"),
            fieldWithPath("[].updated_at").description("Date and time when the task was updated")
    };
}

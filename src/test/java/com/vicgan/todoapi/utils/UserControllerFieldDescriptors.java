package com.vicgan.todoapi.utils;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class UserControllerFieldDescriptors {


    public static final FieldDescriptor[] userApiResponseFd = new FieldDescriptor[]{
            fieldWithPath("id").description("The user id").type(STRING),
            fieldWithPath("full_name").description("user's full name").type(JsonFieldType.STRING),
            fieldWithPath("created_at").description("Creation date and time"),
            fieldWithPath("updated_at").description("Date and time when the user was updated")
    };

    public static final FieldDescriptor[] listOfUsersApiResponseFd = new FieldDescriptor[]{
            fieldWithPath("[].id").description("The user id").type(STRING),
            fieldWithPath("[].full_name").description("user's full name").type(JsonFieldType.STRING),
            fieldWithPath("[].created_at").description("Creation date and time"),
            fieldWithPath("[].updated_at").description("Date and time when the user was updated")
    };
    public static final FieldDescriptor[] updateUserFd = new FieldDescriptor[]{
            fieldWithPath("id").description("The user id").type(STRING),
            fieldWithPath("first_name").description("The user's first name").type(STRING),
            fieldWithPath("last_name").description("The user's last name").type(STRING)
    };
}

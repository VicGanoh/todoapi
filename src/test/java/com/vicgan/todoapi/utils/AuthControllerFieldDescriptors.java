package com.vicgan.todoapi.utils;

import org.springframework.restdocs.payload.FieldDescriptor;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class AuthControllerFieldDescriptors {

    public static final FieldDescriptor[] signInFd = new FieldDescriptor[]{
            fieldWithPath("email").description("user's email to authenticate user"),
            fieldWithPath("password").description("user's password to authenticate user")
    };

    public static final FieldDescriptor[] registerUserFd = new FieldDescriptor[]{
            fieldWithPath("first_name").description("user's first name"),
            fieldWithPath("last_name").description("user's last name"),
            fieldWithPath("email").description("user's email"),
            fieldWithPath("password").description("user's password"),
            fieldWithPath("created_at").description("date and time user was registered").optional().ignored(),
            fieldWithPath("updated_at").description("date and time user information was updated").optional().ignored()
    };
}


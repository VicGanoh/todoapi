package com.vicgan.todoapi.response;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {

    private String token;

    public AuthenticationResponse() {
    }

    private AuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

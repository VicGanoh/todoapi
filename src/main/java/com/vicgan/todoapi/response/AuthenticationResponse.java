package com.vicgan.todoapi.response;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {

    private String access;

    public AuthenticationResponse() {
    }

    private AuthenticationResponse(String access) {
        this.access = access;
    }

    public String getToken() {
        return access;
    }

    public void setToken(String access) {
        this.access = access;
    }
}

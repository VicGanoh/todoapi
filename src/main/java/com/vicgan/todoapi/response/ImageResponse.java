package com.vicgan.todoapi.response;

public class ImageResponse {
    private String message;

    public ImageResponse(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
            this.message = message;
        }
}
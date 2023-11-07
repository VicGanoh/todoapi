package com.vicgan.todoapi.response;

import org.springframework.http.HttpStatus;

import java.util.Objects;

public class ApiError {

    private HttpStatus httpStatus;

    private String errorMessage;

    public ApiError(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiError apiError)) return false;
        return httpStatus == apiError.httpStatus && Objects.equals(errorMessage, apiError.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpStatus, errorMessage);
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "httpStatus=" + httpStatus +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}

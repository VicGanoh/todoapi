package com.vicgan.todoapi.controlleradvice;

import com.vicgan.todoapi.exception.AlreadyExistsException;
import com.vicgan.todoapi.exception.NotFoundException;
import com.vicgan.todoapi.response.ApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collection;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = AlreadyExistsException.class)
    public ResponseEntity<Object> handleAlreadyExistsException(AlreadyExistsException ex, WebRequest webRequest){
        ApiError errorResponse = new ApiError(HttpStatus.FORBIDDEN, ex.getLocalizedMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getHttpStatus());
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest webRequest){
        ApiError errorResponse = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getHttpStatus());
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex, WebRequest webRequest){
        ApiError errorResponse = new ApiError(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage());

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest webRequest) {
        ApiError errorResponse = new ApiError(HttpStatus.FORBIDDEN, "You don't have the permission to access this resource");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
}

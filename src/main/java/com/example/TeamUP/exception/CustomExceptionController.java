package com.example.TeamUP.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionController {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> responseException(CustomException e) {

        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }
}

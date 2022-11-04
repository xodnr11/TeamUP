package com.example.TeamUP.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private HttpStatus status;
    private String message;

    public CustomException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}

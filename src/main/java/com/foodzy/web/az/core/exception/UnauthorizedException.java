package com.foodzy.web.az.core.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Ngoc
 * @since 1.0.0
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unauthorized.")
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}

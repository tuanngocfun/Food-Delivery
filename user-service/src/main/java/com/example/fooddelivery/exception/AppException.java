package com.example.fooddelivery.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final AppError error;

    public AppException(AppError error) {
        super(error.getMessage());
        this.error = error;
    }

    public AppException(ErrorCode code, String message) {
        super(message);
        this.error = new AppError(code, message);
    }
}
package com.foodzy.web.az.core.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Implement a {@link RuntimeException} specifically used for the scenario of querying an non-existed item with CRUD
 * Restful API.
 *
 * @author Ngoc
 * @since 0.0.1
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Item not found")
public class ItemNotFoundException extends RuntimeException {

    /**
     * Desired constructor.
     *
     * @param errorMessage The error message describing the exception.
     */
    public ItemNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}


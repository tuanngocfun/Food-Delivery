package com.example.fooddelivery.util;

import org.springframework.validation.BindException;
import jakarta.validation.ConstraintViolationException;

public class CodeUtil {
    public static String getFirstExceptionMessage(BindException ex, String defaultMessage) {
		if (!ex.hasErrors() || ex.getAllErrors().isEmpty()) {
			return defaultMessage;
		}
		return ex.getAllErrors().get(0).getDefaultMessage();
	}

	public static String getFirstExceptionMessage(
		ConstraintViolationException ex, 
		String defaultMessage
		) {
		var firstViolation = ex.getConstraintViolations().stream().findFirst();
		if (firstViolation.isEmpty()) {
			return defaultMessage;
		}
		return firstViolation.get().getMessage();
	}

}

package com.example.fooddelivery.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// @formatter:off
	SERVER_ERROR("Server error", 500),
	SPRING_ERROR("Spring error", 500),
	URL_NOT_FOUND("URL not found", 404),
	ITEM_NOT_FOUND("Item not found", 404),
	ACCESS_DENIED("Access denied", 403),
	UNAUTHORIZED("Unauthorized", 401),
	BAD_REQUEST("Bad request", 400),
	REQUEST_VALIDATION_FAILED("Request validation failed", 400),
	UPLOAD_FILE_TOO_LARGE("Upload file too large", 400);
	// @formatter:on

	private final String message;

	private final int status;

}

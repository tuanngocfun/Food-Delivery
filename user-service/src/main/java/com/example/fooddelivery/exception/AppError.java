package com.example.fooddelivery.exception;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AppError {

	private final ErrorCode code;

	private final String message;

	private final int status;

	@JsonIgnore
	private AppException exception; // Cache linked exception

	public AppError(ErrorCode code) {
		this(code, code.getMessage(), code.getStatus());
	}

	public AppError(ErrorCode code, String message) {
		this(code, message, code.getStatus());
	}

	public ResponseEntity<AppError> toResponseEntity() {
		return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(this);
	}

	public AppException toException() {
		if (exception == null) {
			exception = new AppException(this);
		}
		return exception;
	}

}

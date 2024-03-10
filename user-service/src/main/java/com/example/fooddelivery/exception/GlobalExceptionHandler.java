package com.example.fooddelivery.exception;
import com.example.fooddelivery.util.CodeUtil;

import jakarta.servlet.ServletException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.NestedCheckedException;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

	@ExceptionHandler({ AppException.class })
	public ResponseEntity<AppError> handleAppException(AppException ex) {
		return ex.getError().toResponseEntity();
	}

	@ExceptionHandler({ HttpMediaTypeException.class, HttpMessageConversionException.class, TypeMismatchException.class,
			MissingServletRequestParameterException.class, MissingServletRequestPartException.class })
	public ResponseEntity<AppError> handleBadRequestException(Exception ex) {
		return new AppError(ErrorCode.BAD_REQUEST).toResponseEntity();
	}

	@ExceptionHandler({ BindException.class, ConstraintViolationException.class })
	public ResponseEntity<AppError> handleValidationException(Exception ex) {
		String defaultMessage = ErrorCode.REQUEST_VALIDATION_FAILED.getMessage();
		String message = defaultMessage;
		if (ex instanceof BindException castedEx) {
			message = CodeUtil.getFirstExceptionMessage(castedEx, defaultMessage);
		}
		else if (ex instanceof ConstraintViolationException castedEx) {
			message = CodeUtil.getFirstExceptionMessage(castedEx, defaultMessage);
		}
		return new AppError(ErrorCode.REQUEST_VALIDATION_FAILED, message).toResponseEntity();
	}

	@ExceptionHandler({ MaxUploadSizeExceededException.class })
	public ResponseEntity<AppError> handleUploadFileTooLargeException() {
		return new AppError(ErrorCode.UPLOAD_FILE_TOO_LARGE).toResponseEntity();
	}

	@ExceptionHandler({ AccessDeniedException.class })
	public ResponseEntity<AppError> handleAccessDeniedException() {
		return new AppError(ErrorCode.ACCESS_DENIED).toResponseEntity();
	}

	@ExceptionHandler({ NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class })
	public ResponseEntity<AppError> handleNoHandlerFoundException() {
		return new AppError(ErrorCode.URL_NOT_FOUND).toResponseEntity();
	}

	@ExceptionHandler({ NestedCheckedException.class, NestedRuntimeException.class, ServletException.class })
	public ResponseEntity<AppError> handleOtherSpringException(Exception ex) {
		log.warn("Spring error!", ex);
		return new AppError(ErrorCode.SPRING_ERROR).toResponseEntity();
	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<AppError> handleOtherException(Exception ex) {
		log.error("Server error!", ex);
		return new AppError(ErrorCode.SERVER_ERROR).toResponseEntity();
	}

}

package com.talentbridge.exception;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.talentbridge.dto.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import com.talentbridge.exception.InactiveUserException;

/**
 * Converts application exceptions into consistent HTTP error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(CandidateProfileNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleCandidateProfileNotFound(CandidateProfileNotFoundException exception,
			HttpServletRequest request) {

		return buildErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
	}

	@ExceptionHandler(DuplicateCandidateProfileException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateCandidateProfile(DuplicateCandidateProfileException exception,
			HttpServletRequest request) {

		return buildErrorResponse(HttpStatus.CONFLICT, exception.getMessage(), request);
	}

	@ExceptionHandler({ AuthenticationCredentialsNotFoundException.class, UsernameNotFoundException.class })
	public ResponseEntity<ErrorResponse> handleAuthenticationFailure(RuntimeException exception,
			HttpServletRequest request) {

		return buildErrorResponse(HttpStatus.UNAUTHORIZED, exception.getMessage(), request);
	}

	@ExceptionHandler(InactiveUserException.class)
	public ResponseEntity<ErrorResponse> handleInactiveUser(InactiveUserException exception,
			HttpServletRequest request) {

		return buildErrorResponse(HttpStatus.UNAUTHORIZED, exception.getMessage(), request);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException exception,
			HttpServletRequest request) {

		return buildErrorResponse(HttpStatus.FORBIDDEN, exception.getMessage(), request);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationFailure(MethodArgumentNotValidException exception,
			HttpServletRequest request) {

		String message = exception.getBindingResult().getFieldErrors().stream()
				.map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage()).distinct()
				.collect(Collectors.joining("; "));

		if (message.isBlank()) {
			message = "Request validation failed";
		}

		return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleUnreadableRequest(HttpMessageNotReadableException exception,
			HttpServletRequest request) {

		return buildErrorResponse(HttpStatus.BAD_REQUEST, "Request body is missing or contains invalid data", request);
	}

	@ExceptionHandler(InvalidResumeFileException.class)
	public ResponseEntity<ErrorResponse> handleInvalidResumeFile(InvalidResumeFileException exception,
			HttpServletRequest request) {

		return buildErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
	}

	@ExceptionHandler(MissingServletRequestPartException.class)
	public ResponseEntity<ErrorResponse> handleMissingRequestPart(MissingServletRequestPartException exception,
			HttpServletRequest request) {

		return buildErrorResponse(HttpStatus.BAD_REQUEST, "Resume file is required", request);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ErrorResponse> handleMaximumUploadSize(MaxUploadSizeExceededException exception,
			HttpServletRequest request) {

		return buildErrorResponse(HttpStatus.PAYLOAD_TOO_LARGE, "Resume file must not exceed 5 MB", request);
	}

	@ExceptionHandler(ResumeStorageException.class)
	public ResponseEntity<ErrorResponse> handleResumeStorageFailure(ResumeStorageException exception,
			HttpServletRequest request) {

		LOGGER.error("Resume storage operation failed for request path: {}", request.getRequestURI(), exception);

		return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Resume file operation failed", request);
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException exception,
			HttpServletRequest request) {

		return buildErrorResponse(HttpStatus.UNAUTHORIZED, exception.getMessage(), request);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException exception, HttpServletRequest request) {

		LOGGER.error("Runtime exception for request path: {}", request.getRequestURI(), exception);

		return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception exception, HttpServletRequest request) {

		LOGGER.error("Unexpected server error for request path: {}", request.getRequestURI(), exception);

		return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected server error occurred", request);
	}

	private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message,
			HttpServletRequest request) {

		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), status.value(), status.name(), message,
				request.getRequestURI(), null);

		return ResponseEntity.status(status).body(errorResponse);
	}
}
package com.talentbridge.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.talentbridge.dto.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Converts backend exceptions into consistent HTTP error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Candidate requested a profile that does not exist.
     */
    @ExceptionHandler(CandidateProfileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCandidateProfileNotFound(
            CandidateProfileNotFoundException exception,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request);
    }

    /**
     * Candidate attempted to create another profile.
     */
    @ExceptionHandler(DuplicateCandidateProfileException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateCandidateProfile(
            DuplicateCandidateProfileException exception,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.CONFLICT,
                exception.getMessage(),
                request);
    }

    /**
     * Authentication information is absent or the authenticated user cannot
     * be found.
     */
    @ExceptionHandler({
        AuthenticationCredentialsNotFoundException.class,
        UsernameNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleUnauthorized(
            RuntimeException exception,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                exception.getMessage(),
                request);
    }

    /**
     * The authenticated user does not have permission for the operation.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException exception,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.FORBIDDEN,
                exception.getMessage(),
                request);
    }

    /**
     * Handles @Valid request DTO validation failures.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationFailure(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {

        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError ->
                        fieldError.getField()
                                + ": "
                                + fieldError.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining("; "));

        if (message.isBlank()) {
            message = "Request validation failed";
        }

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                message,
                request);
    }

    /**
     * Handles malformed JSON and invalid date or number formats.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableRequest(
            HttpMessageNotReadableException exception,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Request body is missing or contains invalid data",
                request);
    }

    /**
     * Final fallback for unexpected backend failures.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(
            Exception exception,
            HttpServletRequest request) {

        String message = exception.getMessage();

        if (message == null || message.isBlank()) {
            message = "An unexpected server error occurred";
        }

        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                message,
                request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.name(),
                message,
                request.getRequestURI(),
                null);

        return ResponseEntity
                .status(status)
                .body(errorResponse);
    }
}
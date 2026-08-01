package com.talentbridge.exception;

import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.talentbridge.dto.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Converts application exceptions into consistent HTTP error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles a Candidate Profile that does not exist.
     *
     * HTTP 404 Not Found
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
     * Handles an attempt to create more than one Candidate Profile.
     *
     * HTTP 409 Conflict
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
     * Handles missing authentication information or an authenticated user
     * that cannot be found.
     *
     * HTTP 401 Unauthorized
     */
    @ExceptionHandler({
        AuthenticationCredentialsNotFoundException.class,
        UsernameNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleAuthenticationFailure(
            RuntimeException exception,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                exception.getMessage(),
                request);
    }

    /**
     * Handles inactive accounts and users without the required role.
     *
     * HTTP 403 Forbidden
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
     * Handles validation errors produced by @Valid request DTOs.
     *
     * HTTP 400 Bad Request
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
     * Handles malformed JSON, invalid dates and invalid numeric values.
     *
     * HTTP 400 Bad Request
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
     * Handles invalid resume files, including unsupported extensions,
     * invalid MIME types, unsafe names and invalid file signatures.
     *
     * HTTP 400 Bad Request
     */
    @ExceptionHandler(InvalidResumeFileException.class)
    public ResponseEntity<ErrorResponse> handleInvalidResumeFile(
            InvalidResumeFileException exception,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                request);
    }

    /**
     * Handles multipart requests that do not contain the required file part.
     *
     * HTTP 400 Bad Request
     */
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestPart(
            MissingServletRequestPartException exception,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Resume file is required",
                request);
    }

    /**
     * Handles resume uploads rejected by Spring's multipart size limit.
     *
     * HTTP 413 Payload Too Large
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaximumUploadSize(
            MaxUploadSizeExceededException exception,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.PAYLOAD_TOO_LARGE,
                "Resume file must not exceed 5 MB",
                request);
    }

    /**
     * Handles candidates who have not uploaded a resume or whose stored
     * resume file no longer exists.
     *
     * HTTP 404 Not Found
     */
    @ExceptionHandler(ResumeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResumeNotFound(
            ResumeNotFoundException exception,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request);
    }

    /**
     * Handles unexpected filesystem problems while storing, loading or
     * deleting resume files.
     *
     * Internal filesystem details are logged but not exposed to clients.
     *
     * HTTP 500 Internal Server Error
     */
    @ExceptionHandler(ResumeStorageException.class)
    public ResponseEntity<ErrorResponse> handleResumeStorageFailure(
            ResumeStorageException exception,
            HttpServletRequest request) {

        LOGGER.error(
                "Resume storage operation failed for request path: {}",
                request.getRequestURI(),
                exception);

        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Resume file operation failed",
                request);
    }

    /**
     * Final fallback for unexpected application errors.
     *
     * The full exception is logged, while only a safe message is returned.
     *
     * HTTP 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(
            Exception exception,
            HttpServletRequest request) {

        LOGGER.error(
                "Unexpected server error for request path: {}",
                request.getRequestURI(),
                exception);

        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected server error occurred",
                request);
    }

    /**
     * Creates the common ErrorResponse format.
     */
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
package com.bob.jobportal.controller;

import com.bob.db.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(Exception ex, WebRequest request) {
        String requestDetails = getRequestDetails(request);
        logger.error("Unhandled exception occurred: {}", ex.getMessage(), ex);
        logger.debug("Request details: {}", requestDetails);
        
        ApiResponse<Object> response = new ApiResponse<>(false, "An unexpected error occurred. Please try again later.", null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(
                fieldError -> fieldError.getField(),
                fieldError -> fieldError.getDefaultMessage()
            ));
        
        String validationErrors = errors.entrySet().stream()
            .map(entry -> entry.getKey() + ": " + entry.getValue())
            .collect(Collectors.joining(", "));
            
        logger.warn("Validation failed: {}", validationErrors);
        logger.debug("Request details: {}", getRequestDetails(request));
        
        ApiResponse<Map<String, String>> response = new ApiResponse<>(false, "Validation failed. Please check the errors.", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(RuntimeException ex, WebRequest request) {
        logger.warn("Business exception: {}", ex.getMessage());
        logger.debug("Request details: {}", getRequestDetails(request));

        ApiResponse<Object> response = new ApiResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private String getRequestDetails(WebRequest request) {
        StringBuilder details = new StringBuilder();
        details.append("URL: ").append(request.getDescription(false));
        String[] params = request.getParameterValues(null);
        if (params != null && params.length > 0) {
            details.append(", Parameters: ").append(String.join(", ", params));
        }
        return details.toString();
    }
}

package com.visable.message.exception;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;


@ControllerAdvice
public class HandleControllerAdvice extends RuntimeException {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessages> badRequestException(IllegalArgumentException ex, WebRequest request) {
        ErrorMessages errorMessage = ErrorMessages.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .description( request.getDescription(false))
                .build();

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessages> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorMessages errorMessage = ErrorMessages.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .build();

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorMessages> duplicateKeyException(DuplicateKeyException ex, WebRequest request) {
        ErrorMessages errorMessage = ErrorMessages.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .build();

        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MessageParseException.class)
    public ResponseEntity<ErrorMessages> messageParseException(MessageParseException ex, WebRequest request) {
        ErrorMessages errorMessage = ErrorMessages.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .build();

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessages> globalExceptionHandler(Exception ex, WebRequest request) {
        ErrorMessages errorMessage = ErrorMessages.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .description( request.getDescription(false))
                .build();


        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

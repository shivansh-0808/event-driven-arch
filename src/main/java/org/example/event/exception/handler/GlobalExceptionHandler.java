package org.example.event.exception.handler;

import org.example.event.dto.response.ErrorResponseDto;
import org.example.event.exception.DomainNotFoundException;
import org.example.event.exception.InvalidEventException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidEventException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidEvent(InvalidEventException ex, WebRequest web) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .apiPath(web.getDescription(false))
                .errorCode(HttpStatus.BAD_REQUEST)
                .errorMsg(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DomainNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleDomainNotFound(DomainNotFoundException ex, WebRequest web) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .apiPath(web.getDescription(false))
                .errorCode(HttpStatus.NOT_FOUND)
                .errorMsg(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }
}

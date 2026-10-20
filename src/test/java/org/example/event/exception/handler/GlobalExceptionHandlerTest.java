package org.example.event.exception.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.event.dto.response.ErrorResponseDto;
import org.example.event.exception.DomainNotFoundException;
import org.example.event.exception.InvalidEventException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleInvalidEvent_shouldReturnBadRequestResponse() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/event");
        WebRequest webRequest = new ServletWebRequest(request);

        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleInvalidEvent(
                new InvalidEventException("Event is invalid"), webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Event is invalid", response.getBody().getErrorMsg());
        assertTrue(response.getBody().getApiPath().contains("/api/event"));
    }

    @Test
    void handleDomainNotFound_shouldReturnNotFoundResponse() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/event/123");
        WebRequest webRequest = new ServletWebRequest(request);

        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleDomainNotFound(
                new DomainNotFoundException("Event not found with id: 123"), webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Event not found with id: 123", response.getBody().getErrorMsg());
        assertTrue(response.getBody().getApiPath().contains("/api/event/123"));
    }
}

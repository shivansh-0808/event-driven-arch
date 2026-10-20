package org.example.event.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

import org.example.event.dto.request.EventRequestDto;
import org.example.event.dto.response.EventResponseDto;
import org.example.event.exception.DomainNotFoundException;
import org.example.event.exception.InvalidEventException;
import org.example.event.service.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @Test
    void create_shouldReturnCreatedStatus_whenServiceReturnsEvent() {
        EventRequestDto request = new EventRequestDto();
        request.setName("Order Created");
        request.setDescription("Customer order created successfully");
        request.setApplicationNames(List.of("orders-service"));

        EventResponseDto expectedResponse = EventResponseDto.builder()
                .name("Order Created")
                .description("Customer order created successfully")
                .applicationNames(List.of("orders-service"))
                .build();

        when(eventService.createEvent(request)).thenReturn(expectedResponse);

        ResponseEntity<EventResponseDto> response = eventController.create(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(eventService).createEvent(request);
    }

    @Test
    void create_shouldPropagateInvalidEventException_whenServiceRejectsRequest() {
        EventRequestDto request = new EventRequestDto();
        request.setName(null);
        request.setDescription("Customer order created successfully");

        when(eventService.createEvent(request)).thenThrow(new InvalidEventException("Event is invalid"));

        InvalidEventException exception = assertThrows(InvalidEventException.class,
                () -> eventController.create(request));

        assertEquals("Event is invalid", exception.getMessage());
    }

    @Test
    void getById_shouldReturnEvent_whenMatchingEventExists() {
        UUID id = UUID.randomUUID();
        EventResponseDto expectedResponse = EventResponseDto.builder()
                .name("Payment Received")
                .description("Payment processed successfully")
                .build();

        when(eventService.findEventById(id)).thenReturn(expectedResponse);

        EventResponseDto response = eventController.getById(id);

        assertEquals(expectedResponse, response);
        verify(eventService).findEventById(id);
    }

    @Test
    void getById_shouldPropagateDomainNotFoundException_whenEventDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(eventService.findEventById(id))
                .thenThrow(new DomainNotFoundException("Event not found with id: " + id));

        DomainNotFoundException exception = assertThrows(DomainNotFoundException.class,
                () -> eventController.getById(id));

        assertEquals("Event not found with id: " + id, exception.getMessage());
    }
}

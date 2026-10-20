package org.example.event.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.event.config.client.ApplicationClient;
import org.example.event.config.client.DomainClient;
import org.example.event.dto.request.EventRequestDto;
import org.example.event.dto.response.ApplicationResponseDto;
import org.example.event.dto.response.DomainResponseDto;
import org.example.event.dto.response.EventResponseDto;
import org.example.event.entity.Event;
import org.example.event.exception.DomainNotFoundException;
import org.example.event.exception.InvalidEventException;
import org.example.event.repository.ApplicationEventRelationshipRepository;
import org.example.event.repository.DomainEventRelationshipRepository;
import org.example.event.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ApplicationEventRelationshipRepository applicationEventRelationshipRepository;

    @Mock
    private DomainEventRelationshipRepository domainEventRelationshipRepository;

    @Mock
    private ApplicationClient applicationClient;

    @Mock
    private DomainClient domainClient;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void createEvent_shouldReturnCreatedEvent_whenValidRequestIsProvided() {
        UUID eventId = UUID.randomUUID();
        Event savedEvent = Event.builder()
                .id(eventId)
                .name("Order Created")
                .description("Customer order created successfully")
                .build();

        EventRequestDto request = new EventRequestDto();
        request.setName("Order Created");
        request.setDescription("Customer order created successfully");
        request.setApplicationNames(List.of("orders-service", "billing-service"));
        request.setDomainNames(List.of("sales"));

        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);
        when(applicationClient.getApplicationByName("orders-service"))
                .thenReturn(ApplicationResponseDto.builder().id(UUID.randomUUID()).name("orders-service").build());
        when(applicationClient.getApplicationByName("billing-service"))
                .thenReturn(ApplicationResponseDto.builder().id(UUID.randomUUID()).name("billing-service").build());
        when(domainClient.getDomainByName("sales"))
                .thenReturn(DomainResponseDto.builder().id(UUID.randomUUID()).name("sales").build());

        EventResponseDto result = eventService.createEvent(request);

        assertEquals("Order Created", result.getName());
        assertEquals("Customer order created successfully", result.getDescription());
        assertEquals(List.of("orders-service", "billing-service"), result.getApplicationNames());
        assertEquals(List.of("sales"), result.getDomainNames());

        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventRepository).save(eventCaptor.capture());
        assertEquals("Order Created", eventCaptor.getValue().getName());
        verify(applicationClient, times(2)).getApplicationByName(anyString());
        verify(domainClient).getDomainByName("sales");
        verify(applicationEventRelationshipRepository, times(2)).save(any());
        verify(domainEventRelationshipRepository).save(any());
    }

    @Test
    void createEvent_shouldThrowInvalidEventException_whenNameIsNull() {
        EventRequestDto request = new EventRequestDto();
        request.setName(null);
        request.setDescription("Description available");
        request.setApplicationNames(List.of("orders-service"));

        InvalidEventException exception = assertThrows(InvalidEventException.class,
                () -> eventService.createEvent(request));

        assertEquals("Event is invalid", exception.getMessage());
        verifyNoInteractions(eventRepository);
    }

    @Test
    void createEvent_shouldThrowInvalidEventException_whenDescriptionIsNull() {
        EventRequestDto request = new EventRequestDto();
        request.setName("Order Created");
        request.setDescription(null);
        request.setApplicationNames(List.of("orders-service"));

        InvalidEventException exception = assertThrows(InvalidEventException.class,
                () -> eventService.createEvent(request));

        assertEquals("Event is invalid", exception.getMessage());
        verifyNoInteractions(eventRepository);
    }

    @Test
    void createEvent_shouldThrowInvalidEventException_whenBothApplicationAndDomainListsAreNull() {
        EventRequestDto request = new EventRequestDto();
        request.setName("Order Created");
        request.setDescription("Customer order created successfully");
        request.setApplicationNames(null);
        request.setDomainNames(null);

        InvalidEventException exception = assertThrows(InvalidEventException.class,
                () -> eventService.createEvent(request));

        assertEquals("Event must be associated with at least one application or domain", exception.getMessage());
        verifyNoInteractions(eventRepository);
    }

    @Test
    void createEvent_shouldThrowNullPointerException_whenRequestIsNull() {
        assertThrows(NullPointerException.class, () -> eventService.createEvent(null));
    }

    @Test
    void findEventById_shouldReturnEvent_whenEventExists() {
        UUID eventId = UUID.randomUUID();
        Event event = Event.builder()
                .id(eventId)
                .name("Payment Received")
                .description("Payment processed successfully")
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        EventResponseDto result = eventService.findEventById(eventId);

        assertEquals("Payment Received", result.getName());
        assertEquals("Payment processed successfully", result.getDescription());
        verify(eventRepository).findById(eventId);
    }

    @Test
    void findEventById_shouldThrowDomainNotFoundException_whenEventDoesNotExist() {
        UUID eventId = UUID.randomUUID();
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        DomainNotFoundException exception = assertThrows(DomainNotFoundException.class,
                () -> eventService.findEventById(eventId));

        assertEquals("Event not found with id: " + eventId, exception.getMessage());
    }
}

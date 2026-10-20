package org.example.event.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.example.event.dto.request.EventRequestDto;
import org.example.event.dto.response.EventResponseDto;
import org.example.event.service.EventService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor

public class EventController {


    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponseDto> create(@Valid @RequestBody EventRequestDto request) {

    EventResponseDto ApplicationResponse = eventService.createEvent(request);

        return new ResponseEntity<>(ApplicationResponse,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public EventResponseDto getById(@PathVariable UUID id) {

    	EventResponseDto eventResponseDto = eventService.findEventById(id);
    	return eventResponseDto;
    }

    
}
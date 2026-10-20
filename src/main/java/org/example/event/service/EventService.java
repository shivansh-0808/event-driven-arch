package org.example.event.service;

import java.util.UUID;

import org.example.event.dto.request.EventRequestDto;
import org.example.event.dto.response.EventResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface EventService {

    EventResponseDto createEvent(EventRequestDto eventRequestDto);

    EventResponseDto findEventById(UUID id);

}

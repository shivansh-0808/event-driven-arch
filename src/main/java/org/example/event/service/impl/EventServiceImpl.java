package org.example.event.service.impl;

import java.util.Objects;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.example.event.config.client.ApplicationClient;
import org.example.event.config.client.DomainClient;
import org.example.event.dto.request.EventRequestDto;
import org.example.event.dto.response.ApplicationResponseDto;
import org.example.event.dto.response.DomainResponseDto;
import org.example.event.dto.response.EventResponseDto;
import org.example.event.entity.ApplicationEventRelationship;
import org.example.event.entity.DomainEventRelationship;
import org.example.event.entity.Event;
import org.example.event.exception.DomainNotFoundException;
import org.example.event.exception.InvalidEventException;
import org.example.event.repository.ApplicationEventRelationshipRepository;
import org.example.event.repository.DomainEventRelationshipRepository;
import org.example.event.repository.EventRepository;
import org.example.event.service.EventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

	private final EventRepository eventRepository;
	private final ApplicationEventRelationshipRepository applicationEventRelationshipRepository;
	private final DomainEventRelationshipRepository domainEventRelationshipRepository;
	private final ApplicationClient applicationClient;
	private final DomainClient domainClient;

	@Override
	@Transactional
	public EventResponseDto createEvent(EventRequestDto eventRequestDto) {
		 validateEventData(eventRequestDto);
		Event event = Event.builder()
				.name(eventRequestDto.getName())
				.description(eventRequestDto.getDescription())
				.build();

		Event savedEvent = eventRepository.save(event);

		// Save Application relationships
		for (String applicationName : eventRequestDto.getApplicationNames()) {

			ApplicationResponseDto application =
					applicationClient.getApplicationByName(applicationName);

				ApplicationEventRelationship relationship =
						ApplicationEventRelationship.builder()
								.eventId(savedEvent.getId())
								.applicationId(application.getId())
								.applicationName(application.getName())
								.build();

				applicationEventRelationshipRepository.save(relationship);

		}

		// Domains
		for (String domainName : eventRequestDto.getDomainNames()) {

			DomainResponseDto domain =
					domainClient.getDomainByName(domainName);

			DomainEventRelationship relationship =
					DomainEventRelationship.builder()
							.eventId(savedEvent.getId())
							.domainId(domain.getId())
							.domainName(domain.getName())
							.build();

			domainEventRelationshipRepository.save(relationship);
		}

		return EventResponseDto.builder()
				.name(savedEvent.getName())
				.description(savedEvent.getDescription())
				.applicationNames(eventRequestDto.getApplicationNames())
				.domainNames(eventRequestDto.getDomainNames())
				.build();
	}

	private void validateEventData(EventRequestDto eventRequestDto) {
		if(!Objects.nonNull(eventRequestDto.getDescription()) || (!Objects.nonNull(eventRequestDto.getName()))){
			throw new InvalidEventException("Event is invalid");
		}
		if (eventRequestDto.getApplicationNames() == null && eventRequestDto.getDomainNames() == null) {
			throw new InvalidEventException("Event must be associated with at least one application or domain");
		}
//		if(eventRequestDto.getApplicationNames() != null) {
//			eventRequestDto.getApplicationNames().forEach(applicationName -> {
//				if (!applicationClient.isApplicationExists(applicationName)) {
//					throw new InvalidEventException("Application not found: " + applicationName);
//				}
//			});
//		}
//		if(eventRequestDto.getDomainNames() != null) {
//			eventRequestDto.getDomainNames().forEach(domainName -> {
//				if (!domainClient.isDomainExists(domainName)) {
//					throw new InvalidEventException("Domain not found: " + domainName);
//				}
//			});
//		}
	}

	@Override
	public EventResponseDto findEventById(UUID id) {

		Event eventEntity = eventRepository.findById(id).orElseThrow(() ->
				new DomainNotFoundException(
						"Event not found with id: " + id
				));
		return EventResponseDto.builder()
				.name(eventEntity.getName())
				.description(eventEntity.getDescription())
				.build();
	}

}

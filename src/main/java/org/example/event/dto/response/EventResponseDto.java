package org.example.event.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
public class EventResponseDto {

    private String name;
    private String description;
    private List<String> applicationNames;
    private List<String> domainNames;

}

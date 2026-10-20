package org.example.event.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class EventRequestDto {

    private String name;
    private String description;
    private List<String> applicationNames;
    private List<String> domainNames;

}

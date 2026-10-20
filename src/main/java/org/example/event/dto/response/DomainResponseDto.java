package org.example.event.dto.response;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DomainResponseDto {

    private UUID id;
    private String name;
    private String description;

}


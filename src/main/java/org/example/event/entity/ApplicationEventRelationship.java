package org.example.event.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.*;

@Entity
@Table(
        name = "application_event_relationship",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_event_application",
                        columnNames = {"ev_id", "appl_id"}
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationEventRelationship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "relationship_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "ev_id", nullable = false)
    private UUID eventId;

    @Column(name = "appl_id", nullable = false)
    @NonNull
    private UUID applicationId;

    @Column(name = "appl_name", nullable = false)
    @NonNull
    private String applicationName;
}

package org.example.event.repository;

import org.example.event.entity.DomainEventRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DomainEventRelationshipRepository
        extends JpaRepository<DomainEventRelationship, UUID> {

    List<DomainEventRelationship> findByEventId(UUID eventId);

    List<DomainEventRelationship> findByDomainId(UUID domainId);
}
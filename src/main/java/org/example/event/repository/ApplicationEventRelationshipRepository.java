package org.example.event.repository;

import org.example.event.entity.ApplicationEventRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApplicationEventRelationshipRepository
        extends JpaRepository<ApplicationEventRelationship, UUID> {

    List<ApplicationEventRelationship> findByEventId(UUID eventId);

    List<ApplicationEventRelationship> findByApplicationId(UUID applicationId);
}
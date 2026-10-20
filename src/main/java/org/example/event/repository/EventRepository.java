package org.example.event.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.example.event.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID>{

}

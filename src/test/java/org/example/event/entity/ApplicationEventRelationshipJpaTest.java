//package org.example.event.entity;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.util.UUID;
//
//import org.example.event.repository.ApplicationEventRelationshipRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//@DataJpaTest
//class ApplicationEventRelationshipJpaTest {
//
//    @Autowired
//    private ApplicationEventRelationshipRepository repository;
//
//    @Test
//    void shouldSaveMultipleRelationsForSameEvent() {
//        UUID eventId = UUID.randomUUID();
//
//        repository.save(ApplicationEventRelationship.builder()
//                .eventId(eventId)
//                .applicationId(UUID.randomUUID())
//                .applicationName("orders-service")
//                .build());
//
//        repository.save(ApplicationEventRelationship.builder()
//                .eventId(eventId)
//                .applicationId(UUID.randomUUID())
//                .applicationName("billing-service")
//                .build());
//
//        assertThat(repository.findByEventId(eventId)).hasSize(2);
//    }
//}

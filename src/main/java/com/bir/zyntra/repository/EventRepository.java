package com.bir.zyntra.repository;

import com.bir.zyntra.model.Event;
import com.bir.zyntra.model.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByUserId(UUID userId);

    List<Event> findByStatus(JobStatus status);

    long countByStatus(JobStatus status);

    Optional<Event> findById(UUID eventId);

    @Modifying
    @Query("UPDATE Event e SET e.status = :status WHERE e.id = :eventId")
    void updateStatus(@Param("eventId") UUID eventId, @Param("status") JobStatus status);
}

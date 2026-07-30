package com.intern.booking_event.repository;

import com.intern.booking_event.model.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.ticketTypes "+
        "WHERE(:title IS NULL OR :title = '' OR LOWER(e.title) LIKE LOWER(CONCAT('%', :title, '%'))) "+
            "AND (:category IS NULL OR :category = '' OR e.category = :category) "+
            "AND (:venue IS NULL OR :venue = '' OR LOWER(e.venue) LIKE LOWER(CONCAT('%', :venue, '%'))) "+
            "AND (cast(:startDate as timestamp) IS NULL OR e.startTime >= :startDate) "+
            "AND (cast(:endDate as timestamp) IS NULL OR e.startTime <= :endDate)"
    )
    Page<Event> searchEvents(
            @Param("title") String title,
            @Param("category") String category,
            @Param("venue") String venue,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            Pageable pageable
    );

}
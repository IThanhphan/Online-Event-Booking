package com.intern.booking_event.repository;

import com.intern.booking_event.model.entity.TicketType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TicketTypeRepository extends JpaRepository<TicketType, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT t
    FROM TicketType t
    WHERE t.id = :id
""")
    Optional<TicketType> findByIdForUpdate(@Param("id") Long id);
}

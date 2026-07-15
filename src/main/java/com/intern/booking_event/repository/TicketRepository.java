package com.intern.booking_event.repository;

import com.intern.booking_event.model.entity.TicketType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<TicketType, Long> {

    // Kích hoạt Pessimistic Lock bảo vệ dòng dữ liệu lúc mua vé đa luồng
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM TicketType t WHERE t.id = :id")
    Optional<TicketType> findByIdForUpdate(@Param("id") Long id);

    @Modifying
    @Query("UPDATE TicketType t SET t.soldQuantity = t.soldQuantity - :quantity WHERE t.id = :id")
    int refundTicketQuantity(@Param("id") Long id, @Param("quantity") int quantity);
}
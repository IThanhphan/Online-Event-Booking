package com.intern.booking_event.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    Optional<Booking> findByReference(String reference);

    // Tối ưu hóa hiệu năng bằng JOIN FETCH dữ liệu items và ticketType cùng lúc
    @Query("SELECT DISTINCT b FROM Booking b " +
           "LEFT JOIN FETCH b.items i " +
           "LEFT JOIN FETCH i.ticketType " +
           "WHERE b.customer.id = :customerId " +
           "ORDER BY b.createdAt DESC")
    List<Booking> findByCustomerId(@Param("customerId") Long customerId);
}
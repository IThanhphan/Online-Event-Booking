package com.intern.booking_event.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.intern.booking_event.constant.BookingStatus;
import com.intern.booking_event.model.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    // Updating the status of a booking
    @Modifying
    @Query("UPDATE Booking b SET b.status = :laterStatus WHERE b.id = :id AND b.status = :firstStatus")
    int updateStatus(@Param("id") Long id, @Param("firstStatus") BookingStatus firstStatus, @Param("laterStatus") BookingStatus laterStatus);
}
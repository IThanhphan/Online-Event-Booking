package com.intern.booking_event.repository;

import com.intern.booking_event.constant.BookingStatus;
import com.intern.booking_event.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Booking b SET b.status = :laterStatus " +
           "WHERE b.id = :id AND b.status IN :allowedStatuses")
    int updateStatusFromAllowed(
        @Param("id") Long id, 
        @Param("allowedStatuses") java.util.List<BookingStatus> allowedStatuses, 
        @Param("laterStatus") BookingStatus laterStatus
    );

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Booking b SET b.status = :laterStatus WHERE b.id = :id AND b.status = :firstStatus")
    int updateStatus(@Param("id") Long id, @Param("firstStatus") BookingStatus firstStatus, @Param("laterStatus") BookingStatus laterStatus);
}
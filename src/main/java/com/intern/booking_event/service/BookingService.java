package com.intern.booking_event.service;

import java.io.ByteArrayInputStream;

import com.intern.booking_event.model.dto.response.BookingResponse;

public interface BookingService {
    // API Controller: /api/bookings/{id}/pay
    BookingResponse payBooking(Long id);
    // API Controller: /api/bookings/{id}/cancel
    BookingResponse cancelBooking(Long id);
    // API Controller: /api/bookings/{id}/confirmation
    ByteArrayInputStream confirmBooking(Long id);
}

package com.intern.booking_event.service;

import com.intern.booking_event.model.dto.response.BookingResponse;

public interface BookingService {
    // API Controller: /api/bookings/{id}/pay
    BookingResponse payBooking(Long id);
}

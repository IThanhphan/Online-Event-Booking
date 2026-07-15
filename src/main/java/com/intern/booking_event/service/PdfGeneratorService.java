package com.intern.booking_event.service;

import java.io.ByteArrayInputStream;

import com.intern.booking_event.model.entity.Booking;

public interface PdfGeneratorService {
    ByteArrayInputStream generateBookingInvoice(Booking booking);
}

package com.intern.booking_event.service;

import java.io.ByteArrayInputStream;

import com.intern.booking_event.model.entity.Booking;

public interface PdfGeneratorService {
    // Generate a PDF file
    ByteArrayInputStream generateBookingInvoice(Booking booking);
}

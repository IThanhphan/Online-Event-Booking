package com.intern.booking_event.service;

import com.intern.booking_event.model.entity.Booking;

public interface RefundService {
    void processRefund(Booking booking);
}
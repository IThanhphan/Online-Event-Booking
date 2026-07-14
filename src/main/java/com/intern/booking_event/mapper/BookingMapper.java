package com.intern.booking_event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.intern.booking_event.model.dto.request.BookingRequest;
import com.intern.booking_event.model.dto.response.BookingResponse;
import com.intern.booking_event.model.entity.Booking;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {
    BookingResponse toBookingResponse(Booking booking);

    Booking toBooking(BookingRequest bookingXRequest);
}

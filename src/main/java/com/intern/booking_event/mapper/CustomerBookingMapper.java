package com.intern.booking_event.mapper;

import com.intern.booking_event.model.dto.response.CustomerBookingsResponse;
import com.intern.booking_event.model.entity.Booking;
import com.intern.booking_event.model.entity.BookingItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerBookingMapper {
    @Mapping(target = "status", expression = "java(booking.getStatus().name())")
    @Mapping(target = "items", source = "items")
    CustomerBookingsResponse toResponse(Booking booking);

    @Mapping(target = "ticketTypeId", source = "ticketType.id")
    @Mapping(target = "ticketTypeName", source = "ticketType.name")
    CustomerBookingsResponse.ItemResponse toItemResponse(BookingItem bookingItem);
}

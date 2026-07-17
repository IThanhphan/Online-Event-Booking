package com.intern.booking_event.mapper;

import com.intern.booking_event.model.dto.request.TicketRequest;
import com.intern.booking_event.model.dto.response.TicketResponse;
import com.intern.booking_event.model.entity.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketTypeMapper {

    @Mapping(target = "eventId", source = "event.id")
    TicketResponse toTicketResponse(TicketType ticketType);

    TicketType toTicketType(TicketRequest request);
}

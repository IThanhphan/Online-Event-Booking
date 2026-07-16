package com.intern.booking_event.mapper;

import com.intern.booking_event.model.dto.request.EventRequest;
import com.intern.booking_event.model.dto.request.TicketRequest;
import com.intern.booking_event.model.dto.response.EventResponse;
import com.intern.booking_event.model.dto.response.TicketResponse;
import com.intern.booking_event.model.entity.Event;
import com.intern.booking_event.model.entity.TicketType;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring", uses = {TicketTypeMapper.class})
public interface EventMapper {
    EventResponse toEventResponse(Event event);
    Event toEvent(EventRequest request);

    //Thiết lập liên kết ngược lại từ con trỏ về cha trước khi lưu
    @AfterMapping
    default void linkTicketType(@MappingTarget Event event) {
        if(event.getTicketTypes() != null)
            event.getTicketTypes().forEach(ticketType -> ticketType.setEvent(event));
    }

}

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


@Mapper(componentModel = "spring")
public interface EventMapper {
    EventResponse toEventResponse(Event event);
    Event toEvent(EventRequest request);

    //Ánh xạ loại vé và tính toán số lượng còn lại
    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "availableQuantity", expression =
            "java(ticketType.getTotalQuantity() - ticketType.getSoldQuantity())")
    TicketResponse toTicketResponse(TicketType ticketType);
    TicketType toTicketType(TicketRequest request);

    //Thiết lập liên kết ngược lại từ con trỏ về cha trước khi lưu
    @AfterMapping
    default void linkTicketType(@MappingTarget Event event) {
        if(event.getTicketTypes() != null)
            event.getTicketTypes().forEach(ticketType -> ticketType.setEvent(event));
    }

}

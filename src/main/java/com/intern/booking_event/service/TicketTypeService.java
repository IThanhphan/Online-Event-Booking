package com.intern.booking_event.service;

import com.intern.booking_event.model.entity.TicketType;

import java.util.List;

public interface TicketTypeService {
    List<TicketType> saveAll(List<TicketType> ticketTypes);
}

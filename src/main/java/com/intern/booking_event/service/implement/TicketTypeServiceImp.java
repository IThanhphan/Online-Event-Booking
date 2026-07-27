package com.intern.booking_event.service.implement;

import com.intern.booking_event.model.entity.TicketType;
import com.intern.booking_event.repository.TicketRepository;
import com.intern.booking_event.service.TicketTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class TicketTypeServiceImp implements TicketTypeService {

    private final TicketRepository ticketRepository;

    @Override
    @Transactional
    public List<TicketType> saveAll(List<TicketType> ticketTypes) {
        return ticketRepository.saveAll(ticketTypes);
    }
}

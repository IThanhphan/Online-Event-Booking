package com.intern.booking_event.service.implement;

import com.intern.booking_event.mapper.EventMapper;
import com.intern.booking_event.model.dto.request.EventRequest;
import com.intern.booking_event.model.dto.response.EventResponse;
import com.intern.booking_event.model.entity.Event;
import com.intern.booking_event.model.entity.TicketType;
import com.intern.booking_event.repository.EventRepository;

import com.intern.booking_event.service.EventService;
import com.intern.booking_event.service.TicketTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImp implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final TicketTypeService ticketTypeService;

    // 1. Hàm tạo sự kiện kèm danh sách loại vé
    @Override
    @Transactional
    public EventResponse createEvent(EventRequest request) {
        Event event = eventMapper.toEvent(request);

        List<TicketType> ticketTypes = event.getTicketTypes();
        event.setTicketTypes(null);

        Event savedEvent = eventRepository.save(event);
        if(ticketTypes != null && !ticketTypes.isEmpty()) {
            ticketTypes.forEach(t -> t.setEvent(savedEvent));

            List<TicketType> saveTicketType =  ticketTypeService.saveAll(ticketTypes);
            savedEvent.setTicketTypes(saveTicketType);
        }
        return eventMapper.toEventResponse(savedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponse> getEvent(String title, String category, String venue,
                                        Instant startDate, Instant endDate,
                                        int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return eventRepository.searchEvents(title, category, venue, startDate, endDate, pageable)
                .map(eventMapper::toEventResponse);
    }

    @Override
    @Transactional
    public EventResponse getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sự kiện với ID: " + id));
        return eventMapper.toEventResponse(event);
    }
}




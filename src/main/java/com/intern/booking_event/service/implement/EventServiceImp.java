package com.intern.booking_event.service.implement;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intern.booking_event.exception.ResourceNotFoundException;
import com.intern.booking_event.mapper.EventMapper;
import com.intern.booking_event.model.dto.request.EventRequest;
import com.intern.booking_event.model.dto.request.EventSearchRequest;
import com.intern.booking_event.model.dto.response.EventResponse;
import com.intern.booking_event.model.entity.Event;
import com.intern.booking_event.model.entity.TicketType;
import com.intern.booking_event.repository.EventRepository;
import com.intern.booking_event.service.EventService;
import com.intern.booking_event.service.TicketTypeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventServiceImp implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final TicketTypeService ticketTypeService;

    @Override
    @Transactional
    public EventResponse createEvent(EventRequest request) {
        Event event = eventMapper.toEvent(request);

        List<TicketType> ticketTypes = event.getTicketTypes();
        event.setTicketTypes(null);

        Event savedEvent = eventRepository.save(event);
        if (ticketTypes != null && !ticketTypes.isEmpty()) {
            ticketTypes.forEach(t -> t.setEvent(savedEvent));

            List<TicketType> saveTicketType = ticketTypeService.saveAll(ticketTypes);
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

        // Truyền thêm param maxPrice = null cho hàm repository chung
        return eventRepository.searchEvents(title, category, venue, null, startDate, endDate, pageable)
                .map(eventMapper::toEventResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponse getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sự kiện với ID: " + id));
        return eventMapper.toEventResponse(event);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponse> searchEvents(EventSearchRequest request, Pageable pageable) {
        
        String title = request != null ? request.getTitle() : null;
        String category = request != null ? request.getCategory() : null;
        String venue = request != null ? request.getVenue() : null;
        BigDecimal maxPrice = request != null ? request.getMaxPrice() : null;
        Instant startDate = request != null ? request.getStartTime() : null; // Map startTime sang startDate
        Instant endDate = request != null ? request.getEndTime() : null;     // Map endTime sang endDate

        Page<Event> eventPage = eventRepository.searchEvents(
            title, category, venue, maxPrice, startDate, endDate, pageable
        );

        return eventPage.map(eventMapper::toEventResponse);
    }
}
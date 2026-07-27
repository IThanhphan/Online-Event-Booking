package com.intern.booking_event.service;

import com.intern.booking_event.model.dto.request.EventRequest;
import com.intern.booking_event.model.dto.response.EventResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;


public interface EventService {
    EventResponse createEvent(EventRequest request);
    Page<EventResponse> getEvent(String title, String category, String venue,
                                 Instant startDate, Instant endDate,
                                 int page, int size, String sortBy, String sortDir);
    EventResponse getEventById(Long id);
}

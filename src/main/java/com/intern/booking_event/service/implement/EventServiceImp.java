package com.intern.booking_event.service.implement;

import com.intern.booking_event.mapper.EventMapper;
import com.intern.booking_event.model.dto.request.EventRequest;
import com.intern.booking_event.model.dto.response.EventResponse;
import com.intern.booking_event.model.entity.Event;
import com.intern.booking_event.repository.EventRepository;

import com.intern.booking_event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
@Service
@RequiredArgsConstructor
public class EventServiceImp implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    // 1. Hàm tạo sự kiện kèm danh sách loại vé
    @Override
    @Transactional
    public EventResponse createEvent(EventRequest request) {
        Event event = eventMapper.toEvent(request);
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toEventResponse(savedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponse> getEvent(String title, String category, String venue,
                                        Instant startDate, Instant endDate, Pageable pageable) {
        Specification<Event> spec = (root, query, cb) -> cb.conjunction();
        if (title != null && !title.trim().isEmpty()) {
            spec = spec.and((root, query, cb)
                    -> cb.like(cb.lower(root.get("title")), "%" + title.trim().toLowerCase() + "%"));
        }
        if (category != null && !category.trim().isEmpty()) {
            spec = spec.and((root, query, cb)
                    -> cb.equal(root.get("category"), category.trim()));
        }
        if (venue != null && !venue.trim().isEmpty()) {
            spec = spec.and((root, query, cb)
                    -> cb.like(cb.lower(root.get("venue")), "%" + venue.trim().toLowerCase() + "%"));
        }
        if (startDate != null) {
            spec = spec.and((root, query, cb)
                    -> cb.greaterThanOrEqualTo(root.get("startTime"), startDate));
        }
        if (endDate != null) {
            spec = spec.and((root, query, cb)
                    -> cb.lessThanOrEqualTo(root.get("startTime"), endDate));
        }
        return eventRepository.findAll(spec, pageable).map(eventMapper::toEventResponse);
    }

    @Override
    @Transactional
    public EventResponse getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sự kiện với ID: " + id));
        return eventMapper.toEventResponse(event);
    }
}




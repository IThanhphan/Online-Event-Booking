package com.intern.booking_event.controller;

import com.intern.booking_event.model.dto.request.EventRequest;
import com.intern.booking_event.model.dto.response.ApiResponse;
import com.intern.booking_event.model.dto.response.EventResponse;
import com.intern.booking_event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ApiResponse<EventResponse> createEvent(@RequestBody EventRequest request) {
        return ApiResponse.<EventResponse>builder()
                .result(eventService.createEvent(request))
                .build();
    }

    @GetMapping
    public ApiResponse<Page<EventResponse>> getEvent(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String venue,
            @RequestParam(required = false) Instant startDate,
            @RequestParam(required = false) Instant endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {

        return ApiResponse.<Page<EventResponse>>builder()
                .result(eventService.getEvent(title, category, venue, startDate, endDate,
                        page, size, sortBy, sortDir))
                .build();
    }

    @GetMapping("/{id}")
    private ApiResponse<EventResponse> getEventById(@PathVariable Long id) {
        return ApiResponse.<EventResponse>builder()
                .result(eventService.getEventById(id))
                .build();
    }
}

package com.intern.booking_event.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventResponse {
    private Long id;
    private String title;
    private String description;
    private String category;
    private String venue;
    private Instant startTime;
    private String organizer;
    private List<TicketResponse> ticketTypes;
}
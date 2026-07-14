package com.intern.booking_event.event.dto;

import lombok.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String category;
    private String venue;
    private Instant startTime;
    private String organizer;
}
package com.intern.booking_event.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiResponse {
//    private String sessionId;
    private String response;
//    private Instant createdAt;
}
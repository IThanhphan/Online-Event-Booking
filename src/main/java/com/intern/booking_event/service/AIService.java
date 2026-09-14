package com.intern.booking_event.service;

import com.intern.booking_event.model.dto.request.AiRequest;
import com.intern.booking_event.model.dto.response.AiResponse;

import java.io.IOException;

public interface AIService {
    AiResponse searchEventWithAi(AiRequest request);
    AiResponse askEventIdWithAi (Long eventId, AiRequest request);
}

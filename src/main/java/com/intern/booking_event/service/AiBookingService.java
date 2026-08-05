package com.intern.booking_event.service;

import com.intern.booking_event.model.dto.request.EventSearchRequest;

public interface AiBookingService {
    
    EventSearchRequest parseSearchPrompt(String userPrompt);

    String answerEventQuestion(Long eventId, String customerQuestion);
}
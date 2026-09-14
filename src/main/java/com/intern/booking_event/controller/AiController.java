package com.intern.booking_event.controller;

import com.intern.booking_event.model.dto.request.AiRequest;
import com.intern.booking_event.model.dto.response.AiResponse;
import com.intern.booking_event.model.dto.response.ApiResponse;
import com.intern.booking_event.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assistant")
@RequiredArgsConstructor
public class AiController {

    private final AIService aiService;

    @PostMapping("/search")
    public ApiResponse<AiResponse> searchEventsWithAi(@RequestBody AiRequest aiRequest) {
        AiResponse response = aiService.searchEventWithAi(aiRequest);
        return ApiResponse.<AiResponse>builder()
                .result(response)
                .build();
    }
}

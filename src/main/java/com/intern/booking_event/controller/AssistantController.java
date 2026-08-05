package com.intern.booking_event.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intern.booking_event.model.dto.request.EventSearchRequest;
import com.intern.booking_event.model.dto.response.ApiResponse;
import com.intern.booking_event.model.dto.response.EventResponse;
import com.intern.booking_event.service.AiBookingService;
import com.intern.booking_event.service.EventService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Tag(name = "AI Assistant", description = "Phân hệ Trợ lý AI hỗ trợ Tìm kiếm sự kiện và Q&A")
public class AssistantController {

    private final AiBookingService aiBookingService;
    private final EventService eventService;

    @PostMapping("/assistant/search")
    @Operation(summary = "AI Natural-Language Event Search (Có phân trang)", 
               description = "Đọc yêu cầu tự nhiên, AI dịch thành tiêu chí lọc (có title) và gọi trực tiếp hàm searchEvents truyền thống")
    public ApiResponse<Page<EventResponse>> searchEventsWithAi(
            @RequestBody String naturalLanguageQuery,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        
        // 1. AI bóc tách prompt ra EventSearchRequest (chứa title, category, venue...)
        EventSearchRequest searchRequest = aiBookingService.parseSearchPrompt(naturalLanguageQuery);
        
        // 2. TẬN DỤNG LẠI HÀM SEARCH TRUYỀN THỐNG CỦA BẠN (Trả về Page<EventResponse>)
        Page<EventResponse> matchedEvents = eventService.searchEvents(searchRequest, pageable);

        return ApiResponse.<Page<EventResponse>>builder()
                .result(matchedEvents)
                .build();
    }

    @PostMapping("/events/{id}/ask")
    @Operation(summary = "AI Event Q&A", 
               description = "Trả lời thắc mắc của khách về một sự kiện cụ thể bằng cách lấy Description làm ngữ cảnh")
    public ApiResponse<String> askAboutEvent(
            @PathVariable Long id, 
            @RequestBody String customerQuestion) {
        
        String answer = aiBookingService.answerEventQuestion(id, customerQuestion);

        return ApiResponse.<String>builder()
                .result(answer)
                .build();
    }
}
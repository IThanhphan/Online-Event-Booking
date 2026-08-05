package com.intern.booking_event.model.dto.request;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSearchRequest {
    private String title;        // Tên hoặc từ khóa tiêu đề sự kiện (được AI bóc tách từ prompt)
    private String category;     // Danh mục: MUSIC, SPORTS, THEATRE, CONFERENCE
    private String venue;        // Địa điểm / Rạp / Thành phố[cite: 3]
    private BigDecimal maxPrice; // Giá vé tối đa
    private Instant startTime;   // Mốc thời gian bắt đầu[cite: 3]
    private Instant endTime;     // Mốc thời gian kết thúc
}
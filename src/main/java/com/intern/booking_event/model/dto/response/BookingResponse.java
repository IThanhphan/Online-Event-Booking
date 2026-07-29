package com.intern.booking_event.model.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {
    private Long id;
    private String status;
    private BigDecimal totalAmount;

}
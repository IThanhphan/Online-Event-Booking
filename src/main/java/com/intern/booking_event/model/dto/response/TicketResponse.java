package com.intern.booking_event.model.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketResponse {
    private Long id;
    private Long eventId;
    private String name;
    private BigDecimal price;
    private int totalQuantity;
    private int soldQuantity;
    private int availableQuantity; // Trường tính toán nhanh: totalQuantity - soldQuantity
}
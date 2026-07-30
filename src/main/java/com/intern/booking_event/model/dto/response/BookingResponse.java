package com.intern.booking_event.model.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {
    private Long id;
    private String reference;
    private Long customerId;
    private String status;
    private BigDecimal totalAmount;
    private Instant createdAt;
    private List<ItemResponse> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemResponse {
        private Long ticketTypeId;
        private String ticketTypeName;
        private int quantity;
        private BigDecimal unitPrice;
    }
}
package com.intern.booking_event.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerBookingsResponse {
    private Long id;
    private String reference;
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
        private Integer quantity;
        private BigDecimal unitPrice;
    }
}
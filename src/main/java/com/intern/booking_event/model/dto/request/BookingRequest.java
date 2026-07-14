package com.intern.booking_event.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {

    @NotNull(message = "ID khách hàng (customerId) không được để null")
    private Long customerId;

    @NotEmpty(message = "Đơn hàng phải có ít nhất một mặt vé (items)")
    @Valid // Kích hoạt validation cho các phần tử bên trong List
    private List<ItemRequest> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemRequest {
        @NotNull(message = "ID loại vé (ticketTypeId) không được để null")
        private Long ticketTypeId;

        @Min(value = 1, message = "Số lượng vé mua tối thiểu của mỗi loại phải từ 1 vé trở lên")
        private int quantity;
    }
}
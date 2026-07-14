package com.intern.booking_event.ticket.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketRequestDTO {

    private Long eventId; // Sẽ được tự động map từ Path Variable trên URL vào

    @NotBlank(message = "Tên loại vé (name) là bắt buộc và không được để trống")
    private String name;

    @NotNull(message = "Giá vé (price) không được để null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Giá vé không được nhỏ hơn 0")
    private BigDecimal price;

    @Min(value = 1, message = "Tổng số lượng vé phát hành (totalQuantity) phải tối thiểu là 1")
    private int totalQuantity;

}
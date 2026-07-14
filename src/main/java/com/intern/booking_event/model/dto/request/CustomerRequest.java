package com.intern.booking_event.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequest {

    private String name; // Khách mua vé ẩn danh có thể để trống trường này lúc checkout

    @NotBlank(message = "Địa chỉ email là bắt buộc và không được để trống")
    @Email(message = "Định dạng email không hợp lệ")
    private String email;
}
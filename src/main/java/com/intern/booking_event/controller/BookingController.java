package com.intern.booking_event.controller;

import java.io.ByteArrayInputStream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intern.booking_event.model.dto.response.ApiResponse;
import com.intern.booking_event.model.dto.response.BookingResponse;
import com.intern.booking_event.service.BookingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
@Tag(name = "Booking Management", description = "Các API điều khiển quy trình đặt chỗ, thanh toán và xuất hóa đơn")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/{id}/pay")
    @Operation(summary = "Thanh toán đơn đặt chỗ", description = "Thay đổi trạng thái đơn đặt chỗ sang đã thanh toán")
    public ApiResponse<BookingResponse> payBooking(
        @Parameter(description = "ID của đơn đặt chỗ", example = "1") @PathVariable Long id){
        return ApiResponse.<BookingResponse>builder()
        .result(bookingService.payBooking(id))
        .build();
    }
    
    @PostMapping("/{id}/cancel")
    @Operation(summary = "Hủy đơn đặt chỗ")
    public ApiResponse<BookingResponse> cancelBooking(
        @Parameter(description = "ID của đơn đặt chỗ", example = "1") @PathVariable Long id){
        return ApiResponse.<BookingResponse>builder()
        .result(bookingService.cancelBooking(id))
        .build();
    }

    @PostMapping("/{id}/confirmation")
    @Operation(summary = "Xác nhận và xuất PDF hóa đơn", description = "Trả về file dạng stream binary để client tải về máy trực tiếp")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200", 
        description = "Xuất hóa đơn PDF thành công",
        content = @Content(
            mediaType = MediaType.APPLICATION_PDF_VALUE,
            schema = @Schema(type = "string", format = "binary")
        )
    )
    public ResponseEntity<InputStreamResource> confirmBooking(
        @Parameter(description = "ID của đơn đặt chỗ", example = "1") @PathVariable Long id) {
        
        ByteArrayInputStream pdfStream = bookingService.confirmBooking(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=booking_confirmation_" + id + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }
}
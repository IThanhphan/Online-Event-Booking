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

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/{id}/pay")
    public ApiResponse<BookingResponse> payBooking(@PathVariable Long id){
        return ApiResponse.<BookingResponse>builder()
        .result(bookingService.payBooking(id))
        .build();
    }
    
    @PostMapping("/{id}/cancel")
    public ApiResponse<BookingResponse> cancelBooking(@PathVariable Long id){
        return ApiResponse.<BookingResponse>builder()
        .result(bookingService.cancelBooking(id))
        .build();
    }

    @PostMapping("/{id}/confirmation")
    public ResponseEntity<InputStreamResource> confirmBooking(@PathVariable Long id) {
        ByteArrayInputStream pdfStream = bookingService.confirmBooking(id);

        HttpHeaders headers = new HttpHeaders();
        // Thiết lập header để trình duyệt tự động kích hoạt hộp thoại tải file xuống
        headers.add("Content-Disposition", "attachment; filename=booking_confirmation_" + id + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }

}

package com.intern.booking_event.model.dto.response;

import com.intern.booking_event.model.dto.request.BookingRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiBookingResponse {
    private String status;
    
    private String message;

    private BookingRequest bookingData;
}

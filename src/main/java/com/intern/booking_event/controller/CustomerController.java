package com.intern.booking_event.controller;

import com.intern.booking_event.model.dto.request.CustomerRequest;
import com.intern.booking_event.model.dto.response.ApiResponse;
import com.intern.booking_event.model.dto.response.CustomerBookingsResponse;
import com.intern.booking_event.model.dto.response.CustomerResponse;
import com.intern.booking_event.service.CustomerService;
import com.intern.booking_event.service.implement.CustomerServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ApiResponse<CustomerResponse> registerCustomer(@RequestBody CustomerRequest request) {
        return ApiResponse.<CustomerResponse>builder()
                .result(customerService.createCustomer(request))
                .build();
    }

    @GetMapping("/{id}/bookings")
    public ApiResponse<List<CustomerBookingsResponse>> getCustomerBookings(@PathVariable Long id) {
        return ApiResponse.<List<CustomerBookingsResponse>>builder()
                .result(customerService.getCustomerBookings(id))
                .build();
    }

}

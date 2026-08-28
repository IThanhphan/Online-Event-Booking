package com.intern.booking_event.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.intern.booking_event.model.dto.request.CustomerRequest;
import com.intern.booking_event.model.dto.response.ApiResponse;
import com.intern.booking_event.model.dto.response.CustomerBookingsResponse;
import com.intern.booking_event.model.dto.response.CustomerResponse;
import com.intern.booking_event.service.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ApiResponse<CustomerResponse> registerCustomer(@Valid @RequestBody CustomerRequest request) {
        return ApiResponse.<CustomerResponse>builder()
                .result(customerService.createCustomer(request))
                .build();
    }


    @GetMapping("/{id}/bookings")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ApiResponse<List<CustomerBookingsResponse>> getCustomerBookings(@PathVariable Long id) {
        return ApiResponse.<List<CustomerBookingsResponse>>builder()
                .result(customerService.getCustomerBookings(id))
                .build();
    }
}

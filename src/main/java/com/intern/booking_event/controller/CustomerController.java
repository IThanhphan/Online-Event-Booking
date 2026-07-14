package com.intern.booking_event.controller;

import com.intern.booking_event.model.dto.request.CustomerRequest;
import com.intern.booking_event.model.dto.response.ApiResponse;
import com.intern.booking_event.model.dto.response.CustomerResponse;
import com.intern.booking_event.service.implement.CustomerServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerServiceImp customerServiceImp;

    @PostMapping
    public ApiResponse<CustomerResponse> registerCustomer(@RequestBody CustomerRequest request) {
        return ApiResponse.<CustomerResponse>builder()
                .result(customerServiceImp.createCustomer(request))
                .build();
    }
}

package com.intern.booking_event.service;

import java.util.List;

import com.intern.booking_event.model.dto.request.CustomerRequest;
import com.intern.booking_event.model.dto.response.CustomerBookingsResponse;
import com.intern.booking_event.model.dto.response.CustomerResponse;

public interface CustomerService {
    // API Controller: /api/customers
    CustomerResponse createCustomer(CustomerRequest request);


    // API Controller: /api/customers/{id}/bookings
    List<CustomerBookingsResponse> getCustomerBookings(Long id);
}

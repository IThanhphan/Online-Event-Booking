package com.intern.booking_event.service.implement;

import com.intern.booking_event.exception.AppException;
import com.intern.booking_event.exception.ErrorCode;
import com.intern.booking_event.mapper.CustomerBookingMapper;
import com.intern.booking_event.mapper.CustomerMapper;
import com.intern.booking_event.model.dto.request.CustomerRequest;
import com.intern.booking_event.model.dto.response.CustomerBookingsResponse;
import com.intern.booking_event.model.dto.response.CustomerResponse;
import com.intern.booking_event.model.entity.Booking;
import com.intern.booking_event.model.entity.Customer;
import com.intern.booking_event.repository.BookingRepository;
import com.intern.booking_event.repository.CustomerRepository;
import com.intern.booking_event.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImp implements CustomerService {
    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;
    private final CustomerBookingMapper customerBookingMapper;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer customer = customerMapper.toCustomer(request);
        return customerMapper.toCustomerResponse(customerRepository.save(customer));
    }

    @Override
    public List<CustomerBookingsResponse> getCustomerBookings(Long id) {
        customerRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));

        List<Booking> customerBookings = bookingRepository.findAllByCustomerId(id);

        return customerBookings.stream()
                .map(customerBookingMapper::toResponse)
                .toList();
    }
}

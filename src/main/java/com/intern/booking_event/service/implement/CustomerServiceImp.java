package com.intern.booking_event.service.implement;

import java.util.HashSet;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intern.booking_event.constant.Role;
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
import com.intern.booking_event.repository.RoleRepository;
import com.intern.booking_event.service.CustomerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImp implements CustomerService {
    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;
    private final RoleRepository roleRepository;
    private final CustomerBookingMapper customerBookingMapper;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        Customer customer = customerMapper.toCustomer(request);
        customer.setPassword(passwordEncoder.encode(request.getPassword()));

        var userRole = roleRepository.findById(Role.USER.name())
                .orElseGet(() -> roleRepository.save(com.intern.booking_event.model.entity.Role.builder()
                        .name(Role.USER.name())
                        .description("User role")
                        .build()));

        HashSet<com.intern.booking_event.model.entity.Role> roles = new HashSet<>();
        roles.add(userRole);
        customer.setRoles(roles);

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

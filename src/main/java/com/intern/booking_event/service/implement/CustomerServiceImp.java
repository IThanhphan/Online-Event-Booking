package com.intern.booking_event.service.implement;

import com.intern.booking_event.mapper.CustomerMapper;
import com.intern.booking_event.model.dto.request.CustomerRequest;
import com.intern.booking_event.model.dto.response.CustomerResponse;
import com.intern.booking_event.model.entity.Customer;
import com.intern.booking_event.repository.CustomerRepository;
import com.intern.booking_event.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImp implements CustomerService {
    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer customer = customerMapper.toCustomer(request);

        return customerMapper.toCustomerResponse(customerRepository.save(customer));
    }
}

package com.intern.booking_event.mapper;

import com.intern.booking_event.model.dto.request.CustomerRequest;
import com.intern.booking_event.model.dto.response.CustomerResponse;
import com.intern.booking_event.model.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {
    CustomerResponse toCustomerResponse(Customer customer);

    Customer toCustomer(CustomerRequest customerRequest);
}

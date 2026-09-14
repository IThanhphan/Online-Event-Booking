package com.intern.booking_event.service;

import com.intern.booking_event.model.dto.request.RoleRequest;
import com.intern.booking_event.model.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse create(RoleRequest request);

    List<RoleResponse> findAll();

    List<RoleResponse> getAll();

    void delete(String role);
}

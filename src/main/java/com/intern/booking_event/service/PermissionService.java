package com.intern.booking_event.service;

import com.intern.booking_event.model.dto.request.PermissionRequest;
import com.intern.booking_event.model.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {
    PermissionResponse create(PermissionRequest request);

    List<PermissionResponse> getAll();

    void delete(String permission);
}

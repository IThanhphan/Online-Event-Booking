package com.intern.booking_event.mapper;

import com.intern.booking_event.model.dto.request.PermissionRequest;
import com.intern.booking_event.model.dto.response.PermissionResponse;
import com.intern.booking_event.model.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}

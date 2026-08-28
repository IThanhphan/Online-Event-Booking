package com.intern.booking_event.mapper;

import com.intern.booking_event.model.dto.request.RoleRequest;
import com.intern.booking_event.model.dto.response.RoleResponse;
import com.intern.booking_event.model.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role Role);
}

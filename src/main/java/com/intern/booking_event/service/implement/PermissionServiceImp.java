package com.intern.booking_event.service.implement;

import java.util.List;

import org.springframework.stereotype.Service;

import com.intern.booking_event.model.dto.request.PermissionRequest;
import com.intern.booking_event.model.dto.response.PermissionResponse;
import com.intern.booking_event.model.entity.Permission;
import com.intern.booking_event.mapper.PermissionMapper;
import com.intern.booking_event.repository.PermissionRepository;
import com.intern.booking_event.service.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImp implements PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    public List<PermissionResponse> getAll() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    @Override
    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }
}

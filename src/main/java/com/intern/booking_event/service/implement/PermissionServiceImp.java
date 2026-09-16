package com.intern.booking_event.service.implement;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intern.booking_event.constant.Role;
import com.intern.booking_event.model.dto.request.PermissionRequest;
import com.intern.booking_event.model.dto.response.PermissionResponse;
import com.intern.booking_event.model.entity.Permission;
import com.intern.booking_event.mapper.PermissionMapper;
import com.intern.booking_event.repository.PermissionRepository;
import com.intern.booking_event.repository.RoleRepository;
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
    RoleRepository roleRepository;

    @Transactional
    @Override
    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);

        // Tự động liên kết quyền mới tạo vào vai trò ADMIN
        final Permission savedPermission = permission;
        roleRepository.findById(Role.ADMIN.name()).ifPresent(adminRole -> {
            if (adminRole.getPermissions() == null) {
                adminRole.setPermissions(new HashSet<>());
            }
            adminRole.getPermissions().add(savedPermission);
            roleRepository.save(adminRole);
            log.info("Đã tự động liên kết quyền [{}] vào vai trò ADMIN", savedPermission.getName());
        });

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

package com.intern.booking_event.service.implement;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.intern.booking_event.model.dto.request.RoleRequest;
import com.intern.booking_event.model.dto.response.RoleResponse;
import com.intern.booking_event.mapper.RoleMapper;
import com.intern.booking_event.repository.PermissionRepository;
import com.intern.booking_event.repository.RoleRepository;
import com.intern.booking_event.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleServiceImp implements RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    @Override
    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);

        if (request.getPermissions() != null) {
            var permissions = permissionRepository.findAllById(request.getPermissions());
            role.setPermissions(new HashSet<>(permissions));
        } else {
            role.setPermissions(new HashSet<>());
        }

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public List<RoleResponse> findAll() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    @Override
    public List<RoleResponse> getAll() {
        return findAll();
    }

    @Override
    public void delete(String role) {
        roleRepository.deleteById(role);
    }
}

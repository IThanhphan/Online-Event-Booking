package com.intern.booking_event.config;

import java.util.HashSet;
import java.util.List;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.intern.booking_event.constant.Role;
import com.intern.booking_event.model.entity.Customer;
import com.intern.booking_event.repository.CustomerRepository;
import com.intern.booking_event.repository.PermissionRepository;
import com.intern.booking_event.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;

    @Bean
    @ConditionalOnProperty(name = "app.init-db", havingValue = "true", matchIfMissing = true)
    ApplicationRunner initApplicationRunner(CustomerRepository customerRepository) {
        return args -> {
            log.info("init ApplicationRunner - Loading permissions, roles, and initializing passwords...");

            // 1. Lấy toàn bộ danh sách permissions có trong MySQL
            var allPermissions = new HashSet<>(permissionRepository.findAll());

            // 2. Tạo hoặc cập nhật Role USER
            com.intern.booking_event.model.entity.Role userRole = roleRepository.findById(Role.USER.name())
                    .orElse(com.intern.booking_event.model.entity.Role.builder()
                            .name(Role.USER.name())
                            .description("User role")
                            .build());
            roleRepository.save(userRole);

            // 3. Tạo hoặc cập nhật Role ADMIN (Gán toàn bộ các permissions vào Admin)
            com.intern.booking_event.model.entity.Role adminRole = roleRepository.findById(Role.ADMIN.name())
                    .orElse(com.intern.booking_event.model.entity.Role.builder()
                            .name(Role.ADMIN.name())
                            .description("Admin role")
                            .build());
            adminRole.setPermissions(allPermissions);
            roleRepository.save(adminRole);

            // 4. Đồng bộ mật khẩu chuẩn cho Admin (admin@gmail.com -> admin123)
            var adminOpt = customerRepository.findByEmail("admin@gmail.com");
            if (adminOpt.isEmpty()) {
                var roles = new HashSet<com.intern.booking_event.model.entity.Role>();
                roles.add(adminRole);

                Customer admin = Customer.builder()
                        .name("Admin")
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("admin123"))
                        .roles(roles)
                        .build();
                customerRepository.save(admin);
                log.info("Admin created with email: admin@gmail.com, password: admin123");
            } else {
                Customer admin = adminOpt.get();
                admin.setPassword(passwordEncoder.encode("admin123"));
                if (admin.getRoles() == null || admin.getRoles().isEmpty()) {
                    admin.setRoles(new HashSet<>(List.of(adminRole)));
                }
                customerRepository.save(admin);
                log.info("Admin password synchronized to: admin123");
            }

            // 5. Đồng bộ mật khẩu cho các tài khoản người dùng mẫu nếu có (123456)
            List<String> sampleUsers = List.of("nguyenvanan@gmail.com", "tranthibich@gmail.com", "lehoanglong@gmail.com", "organizer@gmail.com");
            for (String email : sampleUsers) {
                customerRepository.findByEmail(email).ifPresent(user -> {
                    user.setPassword(passwordEncoder.encode("123456"));
                    customerRepository.save(user);
                });
            }
        };
    }
}

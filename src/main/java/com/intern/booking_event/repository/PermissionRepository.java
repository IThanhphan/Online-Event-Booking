package com.intern.booking_event.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.intern.booking_event.model.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {

    @Query("SELECT p FROM Permission p " +
           "WHERE (:name IS NULL OR :name = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:description IS NULL OR :description = '' OR LOWER(p.description) LIKE LOWER(CONCAT('%', :description, '%')))")
    Page<Permission> searchPermissions(
            @Param("name") String name,
            @Param("description") String description,
            Pageable pageable
    );

    boolean existsByName(String name);

    Optional<Permission> findByName(String name);
}

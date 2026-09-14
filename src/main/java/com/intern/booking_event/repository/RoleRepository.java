package com.intern.booking_event.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.intern.booking_event.model.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    @EntityGraph(attributePaths = {"permissions"})
    @Query("SELECT DISTINCT r FROM Role r " +
           "WHERE (:name IS NULL OR :name = '' OR LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:description IS NULL OR :description = '' OR LOWER(r.description) LIKE LOWER(CONCAT('%', :description, '%')))")
    Page<Role> searchRoles(
            @Param("name") String name,
            @Param("description") String description,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"permissions"})
    @Query("SELECT r FROM Role r WHERE r.name = :name")
    Optional<Role> findByNameWithPermissions(@Param("name") String name);

    @EntityGraph(attributePaths = {"permissions"})
    @Query("SELECT DISTINCT r FROM Role r")
    List<Role> findAllWithPermissions();
}

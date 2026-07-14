package com.intern.booking_event.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    // Tra cứu danh tính ngầm hoặc phục vụ xem lịch sử nhanh qua Email
    Optional<Customer> findByEmail(String email);

    // Tính năng cho Admin: Tìm kiếm không dấu, đẩy kết quả trùng Email lên ưu tiên số 1
    @Query(
        value = "SELECT c FROM Customer c WHERE " +
                "(:keyword IS NULL OR " +
                " LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                " LOWER(FUNCTION('unaccent', c.name)) LIKE LOWER(CONCAT('%', FUNCTION('unaccent', :keyword), '%'))) " +
                "ORDER BY " +
                " CASE WHEN LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) THEN 1 ELSE 2 END ASC, " +
                " c.name ASC",
        countQuery = "SELECT COUNT(c) FROM Customer c WHERE " +
                     "(:keyword IS NULL OR " +
                     " LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                     " LOWER(FUNCTION('unaccent', c.name)) LIKE LOWER(CONCAT('%', FUNCTION('unaccent', :keyword), '%')))"
    )
    Page<Customer> searchCustomersForAdmin(@Param("keyword") String keyword, Pageable pageable);
}
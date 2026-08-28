package com.intern.booking_event.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.intern.booking_event.model.entity.InvalidatedToken;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {

    @Query("SELECT t FROM InvalidatedToken t WHERE t.expiryTime < :now")
    List<InvalidatedToken> findAllExpiredTokens(@Param("now") Date now);

    @Modifying
    @Query("DELETE FROM InvalidatedToken t WHERE t.expiryTime < :now")
    int deleteExpiredTokens(@Param("now") Date now);

    @Query("SELECT t FROM InvalidatedToken t " +
           "WHERE (:id IS NULL OR :id = '' OR t.id LIKE CONCAT('%', :id, '%')) " +
           "AND (cast(:fromDate as timestamp) IS NULL OR t.expiryTime >= :fromDate) " +
           "AND (cast(:toDate as timestamp) IS NULL OR t.expiryTime <= :toDate)")
    Page<InvalidatedToken> searchInvalidatedTokens(
            @Param("id") String id,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            Pageable pageable
    );
}

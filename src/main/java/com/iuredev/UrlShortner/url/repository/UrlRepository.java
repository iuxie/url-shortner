package com.iuredev.UrlShortner.url.repository;

import com.iuredev.UrlShortner.url.model.UrlModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<UrlModel, Long> {

    Optional<UrlModel> findByShortCode(String shortCode);

    boolean existsByShortCode(String shortCode);

    @Modifying
    @Query("UPDATE UrlModel u SET u.accessCount = u.accessCount + 1 WHERE u.shortCode = :shortCode")
    void incrementAccessCount(@Param("shortCode") String shortCode);

}
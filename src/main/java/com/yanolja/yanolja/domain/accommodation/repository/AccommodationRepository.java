package com.yanolja.yanolja.domain.accommodation.repository;

import com.yanolja.yanolja.domain.accommodation.model.entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    @Query("SELECT a FROM Accommodation a JOIN FETCH a.images where a.id = :id")
    Optional<Accommodation> findAccommodationDetailById(Long id);
}
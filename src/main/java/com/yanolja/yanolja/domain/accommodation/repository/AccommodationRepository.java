package com.yanolja.yanolja.domain.accommodation.repository;

import com.yanolja.yanolja.domain.accommodation.model.entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
}
package com.yanolja.yanolja.domain.booking.repository;

import com.yanolja.yanolja.domain.booking.model.entity.Booking;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long>, BookingRepositoryCustom {

    Page<Booking> findAllByUserId(Long userId, Pageable pageable);
}
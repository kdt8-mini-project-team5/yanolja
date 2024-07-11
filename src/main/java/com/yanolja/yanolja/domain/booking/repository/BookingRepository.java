package com.yanolja.yanolja.domain.booking.repository;

import com.yanolja.yanolja.domain.booking.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
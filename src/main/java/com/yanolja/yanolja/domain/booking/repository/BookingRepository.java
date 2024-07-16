package com.yanolja.yanolja.domain.booking.repository;

import com.yanolja.yanolja.domain.booking.model.entity.Booking;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(
        value = "SELECT COUNT(*) FROM booking WHERE room_id = :roomId " +
            "AND (check_in_datetime < :checkOutDatetime AND check_out_datetime > :checkInDatetime)",
        nativeQuery = true
    )
    long checkConflictingBookings(
        @Param("roomId") Long roomId,
        @Param("checkInDatetime") LocalDateTime checkInDatetime,
        @Param("checkOutDatetime") LocalDateTime checkOutDatetime
    );

}
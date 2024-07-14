package com.yanolja.yanolja.domain.booking.repository;

import com.yanolja.yanolja.domain.booking.model.dto.CreateBookingDto;
import java.time.LocalDateTime;

public interface BookingRepositoryCustom {

    boolean checkConflictingBooking(Long roomId, LocalDateTime checkInDatetime, LocalDateTime checkOutDatetime);
    long createBooking(CreateBookingDto bookingDto);

}

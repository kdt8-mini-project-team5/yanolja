package com.yanolja.yanolja.domain.room.repository;

import com.yanolja.yanolja.domain.booking.model.dto.BookingRoomDetailsDto;
import java.util.Optional;

public interface RoomRepositoryCustom {
    Optional<BookingRoomDetailsDto> findRoomDetailsById(Long roomId);
}

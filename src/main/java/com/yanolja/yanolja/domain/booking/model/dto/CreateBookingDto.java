package com.yanolja.yanolja.domain.booking.model.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CreateBookingDto (
     Long userId,
     String orderId,
     String guestName,
     String guestTel,
     Long roomId,
     int people,
     LocalDateTime checkInDatetime,
     LocalDateTime checkOutDatetime,
     long totalPrice
) {}

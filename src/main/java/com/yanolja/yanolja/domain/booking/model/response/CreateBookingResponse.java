package com.yanolja.yanolja.domain.booking.model.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record CreateBookingResponse(
    List<BookingResult> results
) {

    @Builder
    public record BookingResult(
        String orderId,
        String guestName,
        String guestTel,
        String accommodationTitle,
        String roomTitle,
        String roomImage,
        int minPeople,
        int maxPeople,
        LocalDateTime checkInDatetime,
        LocalDateTime checkOutDatetime,
        long totalPrice
    ) {

    }
}

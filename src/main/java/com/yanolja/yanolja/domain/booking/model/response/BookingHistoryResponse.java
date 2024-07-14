package com.yanolja.yanolja.domain.booking.model.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record BookingHistoryResponse(
    List<BookingHistory> bookingHistoryList,
    long totalElements
) {
    @Builder
    public record BookingHistory(
        String orderId,
        String guestName,
        String guestTel,
        String accommodationTitle,
        String roomTitle,
        String roomImg,
        int minPeople,
        int maxPeople,
        LocalDateTime checkInDatetime,
        LocalDateTime checkOutDatetime,
        long totalPrice
    ) {

    }
}

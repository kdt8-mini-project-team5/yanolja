package com.yanolja.yanolja.domain.booking.model.dto;

import java.time.LocalTime;
import lombok.Builder;

@Builder
public record BookingRoomDetailsDto (
    String accommodationTitle,
    String roomTitle,
    long price,
    int minPeople,
    int maxPeople,
    LocalTime checkInTime,
    LocalTime checkOutTime
) {}
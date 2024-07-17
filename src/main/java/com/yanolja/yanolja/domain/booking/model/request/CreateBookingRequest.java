package com.yanolja.yanolja.domain.booking.model.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record CreateBookingRequest(
    @NotNull Long roomId,
    @Positive int people,
    @NotNull @FutureOrPresent LocalDate checkInDate,
    @NotNull @Future LocalDate checkOutDate,
    @NotNull String guestName,
    @NotNull String guestTel
) {}

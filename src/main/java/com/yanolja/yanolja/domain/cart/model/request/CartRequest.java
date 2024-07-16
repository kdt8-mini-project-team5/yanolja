package com.yanolja.yanolja.domain.cart.model.request;

import com.yanolja.yanolja.global.validation.type.CheckInCheckOutDateValidation;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@CheckInCheckOutDateValidation(checkInDate = "checkInDate",checkOutDate = "checkOutDate")
public record CartRequest(
    @NotNull Long roomId,
    @NotNull int people,
    @NotNull LocalDate checkInDate,
    @NotNull LocalDate checkOutDate
) {
}

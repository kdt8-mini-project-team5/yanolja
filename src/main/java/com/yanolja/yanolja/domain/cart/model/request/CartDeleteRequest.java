package com.yanolja.yanolja.domain.cart.model.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CartDeleteRequest(
    @NotNull List<Long> cartList
) {

}

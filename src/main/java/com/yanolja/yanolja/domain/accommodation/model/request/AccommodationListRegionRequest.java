package com.yanolja.yanolja.domain.accommodation.model.request;

import com.yanolja.yanolja.global.validation.type.RegionValidation;
import jakarta.validation.constraints.Max;

public record AccommodationListRegionRequest(
    @RegionValidation String region,
    Long cursorMinPrice,
    Long cursorId,
    @Max(value = 20, message = "size는 최대 {value}입니다.") int size
) {

}

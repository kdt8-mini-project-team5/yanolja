package com.yanolja.yanolja.domain.accommodation.model.response;

public record AccommodationSearchResponse (
    Long id,
    String title,
    Long minPrice,
    String region
) {}
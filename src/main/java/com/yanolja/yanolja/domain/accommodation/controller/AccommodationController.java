package com.yanolja.yanolja.domain.accommodation.controller;

import com.yanolja.yanolja.domain.accommodation.exception.AccommodationException;
import com.yanolja.yanolja.domain.accommodation.exception.errorcode.AccommodationErrorCode;
import com.yanolja.yanolja.domain.accommodation.model.response.AccommodationDetailResponse;
import com.yanolja.yanolja.domain.accommodation.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accommodation")
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping("/{id}")
    public ResponseEntity<AccommodationDetailResponse> getAccommodation(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate) {

        if (checkInDate != null && checkOutDate != null && checkInDate.isAfter(checkOutDate)) {
            throw new AccommodationException(AccommodationErrorCode.INVALID_DATE);
        }

        AccommodationDetailResponse accommodationDetailResponse = accommodationService.getAccommodationById(id, checkInDate, checkOutDate);
        return ResponseEntity.ok(accommodationDetailResponse);
    }
}
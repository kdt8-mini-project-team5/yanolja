package com.yanolja.yanolja.domain.accommodation.controller;

import com.yanolja.yanolja.domain.accommodation.exception.AccommodationException;
import com.yanolja.yanolja.domain.accommodation.exception.errorcode.AccommodationErrorCode;
import com.yanolja.yanolja.domain.accommodation.model.response.AccommodationDetailResponse;
import com.yanolja.yanolja.domain.accommodation.model.response.AccommodationSearchResponse;
import com.yanolja.yanolja.domain.accommodation.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accommodation")
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping("/category")
    public ResponseEntity findAccommodationListByCategory(@ModelAttribute @Valid AccommodationListRequest accommodationListRequest){
        AccommodationsResponse response = accommodationService.findByCategory(
            Category.valueOfType(accommodationListRequest.category()),
            accommodationListRequest.cursorId(),
            PageRequest.of(0, accommodationListRequest.size()),
            accommodationListRequest.cursorMinPrice());
        return APIUtil.OK(response);

    @GetMapping("/{id}")
    public ResponseEntity<AccommodationDetailResponse> getAccommodation(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate) {

        AccommodationDetailResponse accommodationDetailResponse = accommodationService.getAccommodationById(id, checkInDate, checkOutDate);
        return ResponseEntity.ok(accommodationDetailResponse);
    }

    @GetMapping("/region")
    public ResponseEntity findAccommodationListByRegion(@ModelAttribute @Valid AccommodationListRegionRequest accommodationListRegionRequest){
        AccommodationsResponse response = accommodationService.findByRegion(
            accommodationListRegionRequest.region(),
            accommodationListRegionRequest.cursorId(),
            PageRequest.of(0,accommodationListRegionRequest.size()),
            accommodationListRegionRequest.cursorMinPrice()
        );
        return APIUtil.OK(response);

    @GetMapping("/search")
    public ResponseEntity<List<AccommodationSearchResponse>> searchAccommodations(@RequestParam String keyword) {
        List<AccommodationSearchResponse> accommodations = accommodationService.searchAccommodations(keyword);
        return ResponseEntity.ok(accommodations);
    }
}
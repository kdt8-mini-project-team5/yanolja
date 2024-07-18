package com.yanolja.yanolja.domain.accommodation.controller;

import com.yanolja.yanolja.domain.accommodation.model.request.AccommodationListRegionRequest;
import com.yanolja.yanolja.domain.accommodation.model.request.AccommodationListRequest;
import com.yanolja.yanolja.domain.accommodation.model.response.AccommodationsResponse;
import com.yanolja.yanolja.domain.accommodation.model.type.Category;
import com.yanolja.yanolja.domain.accommodation.service.AccommodationService;
import com.yanolja.yanolja.global.util.APIUtil;
import com.yanolja.yanolja.domain.accommodation.model.response.AccommodationDetailResponse;
import com.yanolja.yanolja.domain.accommodation.model.response.AccommodationSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

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

        AccommodationDetailResponse accommodationDetailResponse = accommodationService.getAccommodationById(id, checkInDate, checkOutDate);
        return ResponseEntity.ok(accommodationDetailResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AccommodationSearchResponse>> searchAccommodations(@RequestParam String keyword) {
        List<AccommodationSearchResponse> accommodations = accommodationService.searchAccommodations(keyword);
        return ResponseEntity.ok(accommodations);
    }

    @GetMapping("/category")
    public ResponseEntity findAccommodationListByCategory(@ModelAttribute @Valid AccommodationListRequest accommodationListRequest){
        AccommodationsResponse response = accommodationService.findByCategory(
            Category.valueOfType(accommodationListRequest.category()),
            accommodationListRequest.cursorId(),
            PageRequest.of(0, accommodationListRequest.size()),
            accommodationListRequest.cursorMinPrice());
        return APIUtil.OK(response);
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
    }

}
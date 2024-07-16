package com.yanolja.yanolja.domain.accommodation.controller;

import com.yanolja.yanolja.domain.accommodation.model.request.AccommodationListRegionRequest;
import com.yanolja.yanolja.domain.accommodation.model.request.AccommodationListRequest;
import com.yanolja.yanolja.domain.accommodation.model.response.AccommodationsResponse;
import com.yanolja.yanolja.domain.accommodation.model.type.Category;
import com.yanolja.yanolja.domain.accommodation.service.AccommodationService;
import com.yanolja.yanolja.global.util.APIUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

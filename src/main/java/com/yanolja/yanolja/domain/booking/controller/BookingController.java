package com.yanolja.yanolja.domain.booking.controller;

import com.yanolja.yanolja.domain.auth.config.model.CustomUserDetails;
import com.yanolja.yanolja.domain.booking.model.request.CreateBookingRequest;
import com.yanolja.yanolja.domain.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity createImmediateBooking(
        @Valid @RequestBody CreateBookingRequest createBookingRequest,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ResponseEntity.ok().body(
            bookingService.createImmediateBooking(createBookingRequest, customUserDetails)
        );
    }

    @GetMapping("/history")
    public ResponseEntity getBookingHistory(
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "size", defaultValue = "4") int size,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ResponseEntity.ok().body(
            bookingService.getBookingHistory(customUserDetails, page, size)
        );
    }

}

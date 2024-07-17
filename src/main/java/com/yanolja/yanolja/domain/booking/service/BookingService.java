package com.yanolja.yanolja.domain.booking.service;

import com.yanolja.yanolja.domain.auth.annotaion.GetUser;
import com.yanolja.yanolja.domain.auth.config.model.CustomUserDetails;
import com.yanolja.yanolja.domain.auth.repository.UserRepository;
import com.yanolja.yanolja.domain.booking.exception.BookingException;
import com.yanolja.yanolja.domain.booking.exception.errorcode.BookingErrorCode;
import com.yanolja.yanolja.domain.booking.model.dto.BookingRoomDetailsDto;
import com.yanolja.yanolja.domain.booking.model.dto.CreateBookingDto;
import com.yanolja.yanolja.domain.booking.model.request.CreateBookingRequest;
import com.yanolja.yanolja.domain.booking.model.response.BookingHistoryResponse;
import com.yanolja.yanolja.domain.booking.model.response.BookingHistoryResponse.BookingHistory;
import com.yanolja.yanolja.domain.booking.model.response.CreateBookingResponse;
import com.yanolja.yanolja.domain.booking.repository.BookingRepository;
import com.yanolja.yanolja.domain.room.repository.RoomRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @GetUser
    public CreateBookingResponse createImmediateBooking(
        CreateBookingRequest request,
        CustomUserDetails customUserDetails
    ) {
        long userId = userRepository.findByEmail(customUserDetails.getEmail())
            .orElseThrow(() -> new RuntimeException()).getId();

        return createBooking(request, userId);
    }


    public CreateBookingResponse createBooking(
        CreateBookingRequest request, Long userId
    ) {

        BookingRoomDetailsDto roomDetails = roomRepository.findRoomDetailsById(request.roomId())
            .orElseThrow(() -> new BookingException(BookingErrorCode.ROOM_NOT_FOUND));

        if (roomDetails.minPeople() > request.people() || roomDetails.maxPeople() < request.people()) {
            throw new BookingException(BookingErrorCode.WRONG_OPTIONS);
        }

        LocalDateTime checkInDatetime = LocalDateTime.of(request.checkInDate(), roomDetails.checkInTime());
        LocalDateTime checkOutDatetime = LocalDateTime.of(request.checkOutDate(), roomDetails.checkOutTime());

        if(bookingRepository.checkConflictingBooking(request.roomId(), checkInDatetime, checkOutDatetime)) {
            throw new BookingException(BookingErrorCode.CONFLICT_BOOKING);
        }

        long totalPrice = calculateTotalPrice(
            request.checkInDate(),
            request.checkOutDate(),
            roomDetails.price()
        );

        CreateBookingDto bookingDto = CreateBookingDto.builder()
            .userId(userId)
            .orderId(UUID.randomUUID().toString())
            .guestName(request.guestName())
            .guestTel(request.guestTel())
            .roomId(request.roomId())
            .checkInDatetime(checkInDatetime)
            .checkOutDatetime(checkOutDatetime)
            .people(request.people())
            .totalPrice(totalPrice)
            .build();

        bookingRepository.createBooking(bookingDto);

        String roomImage = roomRepository.findRoomImageById(request.roomId())
            .flatMap(images -> images.stream().findFirst())
            .orElse("");

        return CreateBookingResponse.builder()
            .results(Collections.singletonList(
                CreateBookingResponse.BookingResult.builder()
                    .orderId(bookingDto.orderId())
                    .guestName(request.guestName())
                    .guestTel(request.guestTel())
                    .accommodationTitle(roomDetails.accommodationTitle())
                    .roomTitle(roomDetails.roomTitle())
                    .roomImage(roomImage)
                    .minPeople(roomDetails.minPeople())
                    .maxPeople(roomDetails.maxPeople())
                    .checkInDatetime(checkInDatetime)
                    .checkOutDatetime(checkOutDatetime)
                    .totalPrice(totalPrice)
                    .build()
            ))
            .build();
    }

    @GetUser
    @Transactional(readOnly = true)
    public BookingHistoryResponse getBookingHistory(
        CustomUserDetails customUserDetails,
        int page,
        int size
    ) {
        long userId = userRepository.findByEmail(customUserDetails.getEmail())
            .orElseThrow(() -> new RuntimeException()).getId();

        Pageable pageable = PageRequest.of(page, size);
        var bookingList = bookingRepository.findAllByUserId(userId, pageable);

        return BookingHistoryResponse.builder()
            .bookingHistoryList(bookingList.stream()
                .map(booking -> {
                    var roomImages = booking.getRoom().getImages();
                    String firstImg = roomImages != null && !roomImages.isEmpty()
                        ? roomImages.get(0)
                        : "";

                    return BookingHistory.builder()
                        .orderId(booking.getOrderId())
                        .guestName(booking.getGuestName())
                        .guestTel(booking.getGuestTel())
                        .accommodationTitle(booking.getRoom().getAccommodation().getTitle())
                        .roomTitle(booking.getRoom().getTitle())
                        .roomImg(firstImg)
                        .minPeople(booking.getRoom().getMinPeople())
                        .maxPeople(booking.getRoom().getMaxPeople())
                        .checkInDatetime(booking.getCheckInDatetime())
                        .checkOutDatetime(booking.getCheckOutDatetime())
                        .totalPrice(booking.getTotalPrice())
                        .build();
                })
                .collect(Collectors.toList())
            )
            .totalElements(bookingList.getTotalElements())
            .build();
    }

    private long calculateTotalPrice(LocalDate checkInDate, LocalDate checkOutDate, long pricePerNight) {
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return nights * pricePerNight;
    }
}

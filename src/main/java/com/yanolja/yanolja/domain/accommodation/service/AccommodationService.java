package com.yanolja.yanolja.domain.accommodation.service;

import com.yanolja.yanolja.domain.accommodation.exception.AccommodationException;
import com.yanolja.yanolja.domain.accommodation.exception.errorcode.AccommodationErrorCode;
import com.yanolja.yanolja.domain.accommodation.model.entity.Accommodation;
import com.yanolja.yanolja.domain.accommodation.model.response.AccommodationDetailResponse;
import com.yanolja.yanolja.domain.accommodation.repository.AccommodationRepository;
import com.yanolja.yanolja.domain.room.model.entity.Room;
import com.yanolja.yanolja.domain.room.model.response.RoomResponse;
import com.yanolja.yanolja.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final RoomRepository roomRepository;

    @Cacheable(cacheNames = "accommodationDetails", key = "#id", unless = "#result == null")
    @Transactional(readOnly = true)
    public AccommodationDetailResponse getAccommodationById(Long id, LocalDate checkInDate, LocalDate checkOutDate) {
        validateDates(checkInDate, checkOutDate);

        Accommodation accommodation = accommodationRepository.findAccommodationDetailById(id)
                .orElseThrow(() -> new AccommodationException(AccommodationErrorCode.NOT_FOUND));

        List<RoomResponse> roomResponses = getRoomResponses(accommodation, checkInDate, checkOutDate);

        return new AccommodationDetailResponse(
                Double.parseDouble(accommodation.getLongitude()),
                Double.parseDouble(accommodation.getLatitude()),
                accommodation.getTitle(),
                accommodation.getInfo(),
                accommodation.getMinPrice(),
                accommodation.getCheckIn().toString(),
                accommodation.getCheckOut().toString(),
                accommodation.isShower(),
                accommodation.isAirConditioner(),
                accommodation.isTv(),
                accommodation.isPc(),
                accommodation.isInternet(),
                accommodation.isRefrigerator(),
                accommodation.isToiletries(),
                accommodation.isKitchenware(),
                accommodation.isParkingAvailable(),
                accommodation.getAddress(),
                accommodation.getTel(),
                accommodation.isDryer(),
                roomResponses.size(),
                List.copyOf(accommodation.getImages()),
                roomResponses
        );
    }

    private List<RoomResponse> getRoomResponses(Accommodation accommodation, LocalDate checkInDate, LocalDate checkOutDate) {
        LocalDateTime checkInDateTime = checkInDate.atStartOfDay();
        LocalDateTime checkOutDateTime = checkOutDate.atTime(LocalTime.MAX);
        List<Room> availableRooms = roomRepository.findAvailableRoomsWithImages(accommodation.getId(), checkInDateTime, checkOutDateTime);

        return availableRooms.stream()
                .map(room -> new RoomResponse(
                        room.getId(),
                        room.getTitle(),
                        room.getPrice(),
                        room.getMinPeople(),
                        room.getMaxPeople(),
                        List.copyOf(room.getImages())
                )).toList();
    }

    private void validateDates(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate != null && checkOutDate != null && checkInDate.isAfter(checkOutDate)) {
            throw new AccommodationException(AccommodationErrorCode.INVALID_DATE);
        }
    }
}
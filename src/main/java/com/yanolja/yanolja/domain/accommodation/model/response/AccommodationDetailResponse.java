package com.yanolja.yanolja.domain.accommodation.model.response;

import com.yanolja.yanolja.domain.room.model.response.RoomResponse;

import java.util.List;

public record AccommodationDetailResponse(
        double longitude,
        double latitude,
        String title,
        String info,
        long price,
        String checkIn,
        String checkOut,
        boolean shower,
        boolean aircone,
        boolean tv,
        boolean pc,
        boolean internet,
        boolean refrigerator,
        boolean toiletries,
        boolean kitchenware,
        boolean parkingLodging,
        String address,
        String tel,
        boolean dryer,
        int roomCount,
        List<String> img,
        List<RoomResponse> room
) {}
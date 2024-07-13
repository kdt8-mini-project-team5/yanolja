package com.yanolja.yanolja.domain.room.model.response;

import java.util.List;

public record RoomResponse(
        Long roomId,
        String title,
        long price,
        int minPeople,
        int maxPeople,
        List<String> img
) {}
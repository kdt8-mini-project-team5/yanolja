package com.yanolja.yanolja.domain.room.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yanolja.yanolja.domain.accommodation.model.entity.QAccommodation;
import com.yanolja.yanolja.domain.booking.model.dto.BookingRoomDetailsDto;
import com.yanolja.yanolja.domain.room.model.entity.QRoom;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoomRepositoryCustomImpl implements RoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QRoom room = QRoom.room;
    private final QAccommodation accommodation = QAccommodation.accommodation;

    @Override
    public Optional<BookingRoomDetailsDto> findRoomDetailsById(Long roomId) {
        Tuple result = queryFactory
            .select(
                room.title,
                room.price,
                room.minPeople,
                room.maxPeople,
                accommodation.title,
                accommodation.checkIn,
                accommodation.checkOut
            )
            .from(room)
            .innerJoin(room.accommodation, accommodation)
            .where(room.id.eq(roomId))
            .fetchOne();

        if (result == null) {
            return Optional.empty();
        }

        BookingRoomDetailsDto dto = BookingRoomDetailsDto.builder()
            .roomTitle(result.get(room.title))
            .price(result.get(room.price))
            .maxPeople(result.get(room.maxPeople))
            .minPeople(result.get(room.minPeople))
            .accommodationTitle(result.get(accommodation.title))
            .checkInTime(result.get(accommodation.checkIn))
            .checkOutTime(result.get(accommodation.checkOut))
            .build();

        return Optional.of(dto);
    }
}

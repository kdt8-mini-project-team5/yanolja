package com.yanolja.yanolja.domain.booking.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yanolja.yanolja.domain.auth.model.entity.User;
import com.yanolja.yanolja.domain.booking.model.dto.CreateBookingDto;
import com.yanolja.yanolja.domain.booking.model.entity.Booking;
import com.yanolja.yanolja.domain.booking.model.entity.QBooking;
import com.yanolja.yanolja.domain.room.model.entity.Room;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class BookingRepositoryCustomImpl implements BookingRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public boolean checkConflictingBooking(
        Long roomId,
        LocalDateTime checkInDatetime,
        LocalDateTime checkOutDatetime) {

        QBooking booking = QBooking.booking;

        return queryFactory
            .selectOne()
            .from(booking)
            .where(
                booking.room.id.eq(roomId),
                booking.checkInDatetime.lt(checkOutDatetime),
                booking.checkOutDatetime.gt(checkInDatetime)
            )
            .fetchFirst() != null;
    }

    @Transactional
    @Override
    public long createBooking(CreateBookingDto bookingDto) {

        // 연관 엔티티 설정
        User user = entityManager.getReference(User.class, bookingDto.userId());
        Room room = entityManager.getReference(Room.class, bookingDto.roomId());

        Booking booking = Booking.builder()
            .orderId(bookingDto.orderId())
            .guestName(bookingDto.guestName())
            .guestTel(bookingDto.guestTel())
            .people(bookingDto.people())
            .checkInDatetime(bookingDto.checkInDatetime())
            .checkOutDatetime(bookingDto.checkOutDatetime())
            .totalPrice(bookingDto.totalPrice())
            .user(user)
            .room(room)
            .build();

        entityManager.persist(booking);

        return booking.getId();
    }

}

package com.yanolja.yanolja.domain.room.repository;

import com.yanolja.yanolja.domain.room.model.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT DISTINCT r FROM Room r LEFT JOIN FETCH r.images WHERE r.accommodation.id = :accommodationId AND " +
            "NOT EXISTS (SELECT b FROM Booking b WHERE b.room.id = r.id AND " +
            "(b.checkOutDatetime > :checkInDatetime AND b.checkInDatetime < :checkOutDatetime))")
    List<Room> findAvailableRoomsWithImages(@Param("accommodationId") Long accommodationId,
                                            @Param("checkInDatetime") LocalDateTime checkInDatetime,
                                            @Param("checkOutDatetime") LocalDateTime checkOutDatetime);
}
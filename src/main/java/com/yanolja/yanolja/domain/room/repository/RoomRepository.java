package com.yanolja.yanolja.domain.room.repository;

import com.yanolja.yanolja.domain.room.model.entity.Room;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomRepositoryCustom {

    @Query("SELECT DISTINCT r FROM Room r LEFT JOIN FETCH r.images WHERE r.accommodation.id = :accommodationId AND " +
            "NOT EXISTS (SELECT b FROM Booking b WHERE b.room.id = r.id AND " +
            "(b.checkOutDatetime > :checkInDatetime AND b.checkInDatetime < :checkOutDatetime))")
    List<Room> findAvailableRoomsWithImages(@Param("accommodationId") Long accommodationId,
                                            @Param("checkInDatetime") LocalDateTime checkInDatetime,
                                            @Param("checkOutDatetime") LocalDateTime checkOutDatetime);

    @Query("SELECT r.images FROM Room r WHERE r.id = :id AND SIZE(r.images) > 0")
    Optional<List<String>> findRoomImageById(@Param("id") Long id);

    @Query("SELECT r FROM Room r JOIN FETCH r.accommodation WHERE r.id = :id")
    Optional<Room> findRoomAndAccommodationById(Long id);

}
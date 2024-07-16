package com.yanolja.yanolja.domain.room.repository;

import com.yanolja.yanolja.domain.room.model.entity.Room;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT r FROM Room r JOIN FETCH r.accommodation WHERE r.id = :id")
    Optional<Room> findRoomAndAccommodationById(Long id);

}
package com.yanolja.yanolja.domain.room.repository;

import com.yanolja.yanolja.domain.room.model.entity.Room;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomRepositoryCustom {
    @Query("SELECT r.images FROM Room r WHERE r.id = :id AND SIZE(r.images) > 0")
    Optional<List<String>> findRoomImageById(@Param("id") Long id);
}
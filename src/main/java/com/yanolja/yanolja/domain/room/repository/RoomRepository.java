package com.yanolja.yanolja.domain.room.repository;

import com.yanolja.yanolja.domain.room.model.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
package com.yanolja.yanolja.domain.cart.model.entity;

import com.yanolja.yanolja.domain.auth.model.entity.User;
import com.yanolja.yanolja.domain.room.model.entity.Room;
import com.yanolja.yanolja.global.model.entity.BaseTimeStamp;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cart extends BaseTimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;

    @Column
    private int people;

    @Column
    private Long totalPrice;

    @Column
    private LocalDateTime checkInDateTime;

    @Column
    private LocalDateTime checkOutDateTime;

}

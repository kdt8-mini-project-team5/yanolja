package com.yanolja.yanolja.domain.room.model.entity;

import com.yanolja.yanolja.domain.accommodation.model.entity.Accommodation;
import com.yanolja.yanolja.domain.booking.model.entity.Booking;
import com.yanolja.yanolja.global.model.entity.BaseTimeStamp;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room extends BaseTimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private Long price;

    @Column
    private int minPeople;

    @Column
    private int maxPeople;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> images;

    @ManyToOne(fetch = FetchType.LAZY)
    private Accommodation accommodation;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "room")
    private List<Booking> bookingList;
}
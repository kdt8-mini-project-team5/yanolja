package com.yanolja.yanolja.domain.accommodation.model.entity;

import com.yanolja.yanolja.domain.accommodation.model.type.Category;
import com.yanolja.yanolja.domain.room.model.entity.Room;
import com.yanolja.yanolja.global.model.entity.BaseTimeStamp;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Accommodation extends BaseTimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String title;

    @Column
    private String info;

    @Column
    private String address;

    @Column
    private String region;

    @Column
    private String longitude;

    @Column
    private String latitude;

    @Column
    private String tel;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column
    private LocalTime checkIn;

    @Column
    private LocalTime checkOut;

    @Column
    private boolean parkingAvailable;

    @Column
    private boolean tv;

    @Column
    private boolean pc;

    @Column
    private boolean internet;

    @Column
    private boolean refrigerator;

    @Column
    private boolean shower;

    @Column
    private boolean airConditioner;

    @Column
    private boolean cookingAvailable;

    @Column
    private boolean toiletries;

    @Column
    private boolean kitchenware;

    @Column
    private boolean dryer;

    @Column
    private Long minPrice;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> images;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "accommodation")
    private List<Room> roomList;

}
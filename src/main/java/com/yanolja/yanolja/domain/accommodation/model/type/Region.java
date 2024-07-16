package com.yanolja.yanolja.domain.accommodation.model.type;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Region {

    SEOUL("서울"),
    INCHEON("인천"),
    BUSAN("부산"),
    DAEJEON("대전"),
    GWANGJU("광주"),
    DAEGU("대구"),
    SEJONG("세종"),
    KYEONGGI("경기"),
    GANGWON("강원"),
    CHUNGBUK("충청북도"),
    CHUNGNAM("충청남도"),
    GYEONGBUK("경상북도"),
    GYEONGNAM("경상남도"),
    JEONBUK("전북"),
    JEONNAM("전라남도"),
    ULSAN("울산"),
    JEJU("제주"),
    ;

    private final String type;

    private static final Map<String, Region> BY_TYPE =
        Stream.of(values()).collect(Collectors.toMap(Region::getType, Function.identity()));

    public static boolean checkValidCategory(String type) {
        Region region = BY_TYPE.get(type);
        if (region == null){
            return false;
        }
        return true;
    }
}

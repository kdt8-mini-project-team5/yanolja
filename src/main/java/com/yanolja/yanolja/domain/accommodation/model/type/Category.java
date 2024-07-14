package com.yanolja.yanolja.domain.accommodation.model.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public enum Category {

    HOTEL("관광 호텔"),
    KON_DO("콘도 미니엄"),
    YOUTH_HOSTEL("유스 호스텔"),
    PENSION("펜션"),
    MOTEL("모텔"),
    MIN_BACK("민박"),
    GUEST_HOUSE("게스트 하우스"),
    HOME_STAY("홈 스테이"),
    SERVICE_DREGIDENCE("서비스 드레지던스"),
    HAN_OK("한옥");

    private final String type;

    private static final Map<String, Category> BY_TYPE =
        Stream.of(values()).collect(Collectors.toMap(Category::getType, Function.identity()));

    public static Category valueOfType(String type) {
        return BY_TYPE.get(type);
    }

    public static boolean checkValidCategory(String type) {
        Category category = BY_TYPE.get(type);
        if (category == null){
            return false;
        }
        return true;
    }

}

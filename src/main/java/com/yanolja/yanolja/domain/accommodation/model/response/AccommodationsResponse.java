package com.yanolja.yanolja.domain.accommodation.model.response;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;

public record AccommodationsResponse(
    boolean nextData,
    Long nextCursorId,
    Long nextCursorMinPrice,
    List<AccommodationSimpleResponse> accommodationSimpleResponseList) {

    public static AccommodationsResponse createAccommodationsResponse(Page<AccommodationSimpleDTO> accommodationSimpleDTOPage,List<String> accommodationImages){
        List<AccommodationSimpleResponse> responseList = new ArrayList<>();
        for(int i = 0; i < accommodationSimpleDTOPage.getNumberOfElements(); i++){
            responseList.add(AccommodationSimpleResponse.fromDTO(accommodationSimpleDTOPage.getContent().get(i),accommodationImages.get(1+(i*2))));
        }

        return new AccommodationsResponse(
            accommodationSimpleDTOPage.hasNext(),
            responseList.isEmpty() ? null : responseList.get(responseList.size() - 1).id(),
            responseList.isEmpty() ? null : responseList.get(responseList.size() - 1).minPrice(),
            responseList
        );
    }

    public record AccommodationSimpleResponse(
        Long id,
        String title,
        Long minPrice,
        String region,
        String thumbnailUrl
    ) {
        private static AccommodationSimpleResponse fromDTO(AccommodationSimpleDTO accommodationSimpleDTO,String thumbnailUrl) {
            return new AccommodationSimpleResponse(
                accommodationSimpleDTO.id(),
                accommodationSimpleDTO.title(),
                accommodationSimpleDTO.minPrice(),
                accommodationSimpleDTO.region(),
                thumbnailUrl
                );
        }
    }
}

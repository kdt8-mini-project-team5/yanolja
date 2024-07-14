package com.yanolja.yanolja.domain.accommodation.service;

import com.yanolja.yanolja.domain.accommodation.model.response.AccommodationSimpleDTO;
import com.yanolja.yanolja.domain.accommodation.model.response.AccommodationsResponse;
import com.yanolja.yanolja.domain.accommodation.model.type.Category;
import com.yanolja.yanolja.domain.accommodation.repository.AccommodationRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;

    @Cacheable(cacheNames = "accommodationList", key = "#category.name() + ':' + #cursorId + ':' + #cursorMinPrice")
    @Transactional(readOnly = true)
    public AccommodationsResponse findByCategory(Category category, Long cursorId, Pageable pageable, Long cursorMinPrice) {
        Page<AccommodationSimpleDTO> accommodationSimpleDTOPage = getAccommodationSimpleProjectionList(category, cursorId, pageable, cursorMinPrice);

        List<Long> idList = accommodationSimpleDTOPage.stream()
            .map(AccommodationSimpleDTO::id).toList();
        List<String> accommodationImages = getAccommodationImages(idList);

        return AccommodationsResponse.createAccommodationsResponse(accommodationSimpleDTOPage, accommodationImages);
    }

    private Page<AccommodationSimpleDTO> getAccommodationSimpleProjectionList(Category category, Long cursorId, Pageable pageable, Long cursorMinPrice) {
        if (cursorId == null) {
            if (cursorMinPrice == null) {
                return accommodationRepository.findByCategory(category, pageable);
            }else{
                return accommodationRepository.findByCategoryWithCursorMinPrice(category,cursorMinPrice,pageable);
            }
        } else {
            return accommodationRepository.findByCategoryWithCursor(category,cursorMinPrice,cursorId,pageable);
        }
    }

    private List<String> getAccommodationImages(List<Long> ids) {
        String idListString = ids.stream()
            .map(String::valueOf)
            .collect(Collectors.joining(","));
        return accommodationRepository.findAccommodationImagesByIds(ids, idListString);
    }




}

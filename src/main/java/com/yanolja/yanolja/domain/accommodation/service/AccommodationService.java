package com.yanolja.yanolja.domain.accommodation.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.yanolja.yanolja.domain.accommodation.model.response.AccommodationSimpleDTO;
import com.yanolja.yanolja.domain.accommodation.model.response.AccommodationsResponse;
import com.yanolja.yanolja.domain.accommodation.model.type.Category;
import com.yanolja.yanolja.domain.accommodation.exception.AccommodationException;
import com.yanolja.yanolja.domain.accommodation.exception.errorcode.AccommodationErrorCode;
import com.yanolja.yanolja.domain.accommodation.model.entity.Accommodation;
import com.yanolja.yanolja.domain.accommodation.model.entity.AccommodationDocument;
import com.yanolja.yanolja.domain.accommodation.model.response.AccommodationDetailResponse;
import com.yanolja.yanolja.domain.accommodation.model.response.AccommodationSearchResponse;
import com.yanolja.yanolja.domain.accommodation.repository.AccommodationRepository;
import com.yanolja.yanolja.domain.accommodation.repository.AccommodationSearchRepository;
import com.yanolja.yanolja.domain.room.model.entity.Room;
import com.yanolja.yanolja.domain.room.model.response.RoomResponse;
import com.yanolja.yanolja.domain.room.repository.RoomRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AccommodationService {

    @Autowired
    private final AccommodationRepository accommodationRepository;
    private final RoomRepository roomRepository;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Autowired
    private AccommodationSearchRepository accommodationSearchRepository;

    @Cacheable(cacheNames = "accommodationDetails", key = "#id", unless = "#result == null")
    @Transactional(readOnly = true)
    public AccommodationDetailResponse getAccommodationById(Long id, LocalDate checkInDate, LocalDate checkOutDate) {
        validateDates(checkInDate, checkOutDate);

        Accommodation accommodation = accommodationRepository.findAccommodationDetailById(id)
            .orElseThrow(() -> new AccommodationException(AccommodationErrorCode.NOT_FOUND));

        List<RoomResponse> roomResponses = getRoomResponses(accommodation, checkInDate, checkOutDate);

        return new AccommodationDetailResponse(
            Double.parseDouble(accommodation.getLongitude()),
            Double.parseDouble(accommodation.getLatitude()),
            accommodation.getTitle(),
            accommodation.getInfo(),
            accommodation.getMinPrice(),
            accommodation.getCheckIn().toString(),
            accommodation.getCheckOut().toString(),
            accommodation.isShower(),
            accommodation.isAirConditioner(),
            accommodation.isTv(),
            accommodation.isPc(),
            accommodation.isInternet(),
            accommodation.isRefrigerator(),
            accommodation.isToiletries(),
            accommodation.isKitchenware(),
            accommodation.isParkingAvailable(),
            accommodation.getAddress(),
            accommodation.getTel(),
            accommodation.isDryer(),
            roomResponses.size(),
            List.copyOf(accommodation.getImages()),
            roomResponses
        );
    }

    private List<RoomResponse> getRoomResponses(Accommodation accommodation, LocalDate checkInDate, LocalDate checkOutDate) {
        LocalDateTime checkInDateTime = checkInDate.atStartOfDay();
        LocalDateTime checkOutDateTime = checkOutDate.atTime(LocalTime.MAX);
        List<Room> availableRooms = roomRepository.findAvailableRoomsWithImages(accommodation.getId(), checkInDateTime, checkOutDateTime);

        return availableRooms.stream()
            .map(room -> new RoomResponse(
                room.getId(),
                room.getTitle(),
                room.getPrice(),
                room.getMinPeople(),
                room.getMaxPeople(),
                List.copyOf(room.getImages())
            )).toList();
    }

    private void validateDates(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate != null && checkOutDate != null && checkInDate.isAfter(checkOutDate)) {
            throw new AccommodationException(AccommodationErrorCode.INVALID_DATE);
        }
    }

    @Transactional(readOnly = true)
    public List<AccommodationSearchResponse> searchAccommodations(String keyword) {
        SearchRequest searchRequest = getSearchRequest(keyword);
        SearchResponse<AccommodationDocument> searchResponse = loadSearchRequest(searchRequest);

        return searchResponse.hits().hits().stream()
            .map(Hit::source)
            .map(document -> new AccommodationSearchResponse(
                document.getId(),
                document.getTitle(),
                document.getMinPrice(),
                document.getRegion()
            ))
            .toList();
    }

    private SearchRequest getSearchRequest(String keyword) {
        return SearchRequest.of(request -> request
            .index("accommodations")
            .query(query -> query
                .multiMatch(multiMatch -> multiMatch
                    .query(keyword)
                    .fields("title", "region")
                    .fuzziness("AUTO")
                )
            )
        );
    }

    private SearchResponse<AccommodationDocument> loadSearchRequest(SearchRequest searchRequest) {
        try {
            return elasticsearchClient.search(searchRequest, AccommodationDocument.class);
        } catch (IOException e) {
            throw new AccommodationException(AccommodationErrorCode.SEARCH_ERROR);
        }
    }

    @PostConstruct
    @Transactional
    public void init() {
        indexAccommodations();
    }

    @Transactional
    public void indexAccommodations() {
        accommodationRepository.findAll().forEach(accommodation -> {
            AccommodationDocument document = new AccommodationDocument(
                accommodation.getId(),
                accommodation.getTitle(),
                accommodation.getRegion(),
                accommodation.getMinPrice()
            );
            accommodationSearchRepository.save(document);
        });
    }


    @Cacheable(cacheNames = "accommodationListByCategory", key = "#category.name() + ':' + #cursorId + ':' + #cursorMinPrice")
    @Transactional(readOnly = true)
    public AccommodationsResponse findByCategory(Category category, Long cursorId, Pageable pageable, Long cursorMinPrice) {
        Page<AccommodationSimpleDTO> accommodationSimpleDTOPage = getAccommodationSimpleDTOList(category, cursorId, pageable, cursorMinPrice);

        List<String> accommodationImages = getAccommodationImages(accommodationSimpleDTOPage);

        return AccommodationsResponse.createAccommodationsResponse(accommodationSimpleDTOPage, accommodationImages);
    }

    private Page<AccommodationSimpleDTO> getAccommodationSimpleDTOList(Category category, Long cursorId, Pageable pageable, Long cursorMinPrice) {
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

    private List<String> getAccommodationImages(Page<AccommodationSimpleDTO> accommodationSimpleDTOPage) {
        List<Long> idList = accommodationSimpleDTOPage.stream()
            .map(AccommodationSimpleDTO::id).toList();
        String idListString = idList.stream()
            .map(String::valueOf)
            .collect(Collectors.joining(","));
        return accommodationRepository.findAccommodationImagesByIds(idList, idListString);
    }

    @Cacheable(cacheNames = "accommodationListByRegion", key = "#region + ':' + #cursorId + ':' + #cursorMinPrice")
    @Transactional(readOnly = true)
    public AccommodationsResponse findByRegion(String region, Long cursorId, Pageable pageable, Long cursorMinPrice) {
        Page<AccommodationSimpleDTO> accommodationSimpleDTOPage = getAccommodationSimpleDTOListByRegion(region, cursorId, pageable, cursorMinPrice);

        List<String> accommodationImages = getAccommodationImages(accommodationSimpleDTOPage);

        return AccommodationsResponse.createAccommodationsResponse(accommodationSimpleDTOPage, accommodationImages);
    }

    private Page<AccommodationSimpleDTO> getAccommodationSimpleDTOListByRegion(String region, Long cursorId, Pageable pageable, Long cursorMinPrice) {
        if (cursorId == null) {
            if (cursorMinPrice == null) {
                return accommodationRepository.findByRegion(region, pageable);
            }else{
                return accommodationRepository.findByRegionWithCursorMinPrice(region,cursorMinPrice,pageable);
            }
        } else {
            return accommodationRepository.findByRegionWithCursor(region,cursorMinPrice,cursorId,pageable);
        }
    }

}
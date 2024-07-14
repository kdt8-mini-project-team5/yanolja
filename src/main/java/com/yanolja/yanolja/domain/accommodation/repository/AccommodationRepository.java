package com.yanolja.yanolja.domain.accommodation.repository;

import com.yanolja.yanolja.domain.accommodation.model.entity.Accommodation;
import com.yanolja.yanolja.domain.accommodation.model.response.AccommodationSimpleDTO;
import com.yanolja.yanolja.domain.accommodation.model.type.Category;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    @Query("SELECT new com.yanolja.yanolja.domain.accommodation.model.response.AccommodationSimpleDTO(a.id,a.title,a.minPrice,a.region) "
        + "FROM Accommodation a WHERE a.category = :category ORDER BY a.minPrice , a.id DESC")
    Page<AccommodationSimpleDTO> findByCategory(Category category, Pageable pageable);

    @Query(
        value = "SELECT new com.yanolja.yanolja.domain.accommodation.model.response.AccommodationSimpleDTO(a.id,a.title,a.minPrice,a.region) " +
            "FROM Accommodation a " +
            "WHERE a.category = :category AND a.minPrice = :minPrice AND a.id < :cursorId " +
            "UNION ALL " +
            "SELECT new com.yanolja.yanolja.domain.accommodation.model.response.AccommodationSimpleDTO(a.id,a.title,a.minPrice,a.region) " +
            "FROM Accommodation a " +
            "WHERE a.category = :category AND a.minPrice > :minPrice " +
            "ORDER BY a.minPrice, a.id DESC",
        countQuery = "SELECT COUNT(a.id) FROM Accommodation a WHERE a.category = :category AND a.minPrice = :minPrice or a.minPrice > :minPrice"
    )
    Page<AccommodationSimpleDTO> findByCategoryWithCursor(Category category, Long minPrice, Long cursorId, Pageable pageable);

    @Query(
        value = "SELECT new com.yanolja.yanolja.domain.accommodation.model.response.AccommodationSimpleDTO(a.id,a.title,a.minPrice,a.region) " +
            "FROM Accommodation a " +
            "WHERE a.category = :category AND a.minPrice = :minPrice " +
            "UNION ALL " +
            "SELECT new com.yanolja.yanolja.domain.accommodation.model.response.AccommodationSimpleDTO(a.id,a.title,a.minPrice,a.region) " +
            "FROM Accommodation a " +
            "WHERE a.category = :category AND a.minPrice > :minPrice " +
            "ORDER BY a.minPrice, a.id DESC",
        countQuery = "SELECT COUNT(a.id) FROM Accommodation a WHERE a.category = :category AND a.minPrice = :minPrice or a.minPrice > :minPrice"
    )
    Page<AccommodationSimpleDTO> findByCategoryWithCursorMinPrice(Category category, Long minPrice, Pageable pageable);



    @Query("SELECT a.images FROM Accommodation a WHERE a.id in :ids order by FIND_IN_SET(a.id,:idsString)")
    List<String> findAccommodationImagesByIds(List<Long> ids, String idsString);
}
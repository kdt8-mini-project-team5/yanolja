package com.yanolja.yanolja.domain.accommodation.repository;

import com.yanolja.yanolja.domain.accommodation.model.entity.AccommodationDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AccommodationSearchRepository extends ElasticsearchRepository<AccommodationDocument, Long> {
}
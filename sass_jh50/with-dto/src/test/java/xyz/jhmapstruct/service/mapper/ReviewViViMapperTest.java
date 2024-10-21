package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ReviewViViAsserts.*;
import static xyz.jhmapstruct.domain.ReviewViViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReviewViViMapperTest {

    private ReviewViViMapper reviewViViMapper;

    @BeforeEach
    void setUp() {
        reviewViViMapper = new ReviewViViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReviewViViSample1();
        var actual = reviewViViMapper.toEntity(reviewViViMapper.toDto(expected));
        assertReviewViViAllPropertiesEquals(expected, actual);
    }
}

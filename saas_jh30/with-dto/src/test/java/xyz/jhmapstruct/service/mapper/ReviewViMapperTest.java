package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ReviewViAsserts.*;
import static xyz.jhmapstruct.domain.ReviewViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReviewViMapperTest {

    private ReviewViMapper reviewViMapper;

    @BeforeEach
    void setUp() {
        reviewViMapper = new ReviewViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReviewViSample1();
        var actual = reviewViMapper.toEntity(reviewViMapper.toDto(expected));
        assertReviewViAllPropertiesEquals(expected, actual);
    }
}

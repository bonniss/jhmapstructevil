package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ReviewBetaAsserts.*;
import static xyz.jhmapstruct.domain.ReviewBetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReviewBetaMapperTest {

    private ReviewBetaMapper reviewBetaMapper;

    @BeforeEach
    void setUp() {
        reviewBetaMapper = new ReviewBetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReviewBetaSample1();
        var actual = reviewBetaMapper.toEntity(reviewBetaMapper.toDto(expected));
        assertReviewBetaAllPropertiesEquals(expected, actual);
    }
}

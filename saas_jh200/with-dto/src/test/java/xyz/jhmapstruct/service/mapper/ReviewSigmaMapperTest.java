package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ReviewSigmaAsserts.*;
import static xyz.jhmapstruct.domain.ReviewSigmaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReviewSigmaMapperTest {

    private ReviewSigmaMapper reviewSigmaMapper;

    @BeforeEach
    void setUp() {
        reviewSigmaMapper = new ReviewSigmaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReviewSigmaSample1();
        var actual = reviewSigmaMapper.toEntity(reviewSigmaMapper.toDto(expected));
        assertReviewSigmaAllPropertiesEquals(expected, actual);
    }
}

package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ReviewThetaAsserts.*;
import static xyz.jhmapstruct.domain.ReviewThetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReviewThetaMapperTest {

    private ReviewThetaMapper reviewThetaMapper;

    @BeforeEach
    void setUp() {
        reviewThetaMapper = new ReviewThetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReviewThetaSample1();
        var actual = reviewThetaMapper.toEntity(reviewThetaMapper.toDto(expected));
        assertReviewThetaAllPropertiesEquals(expected, actual);
    }
}

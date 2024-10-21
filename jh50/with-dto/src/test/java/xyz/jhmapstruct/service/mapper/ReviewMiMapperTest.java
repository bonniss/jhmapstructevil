package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ReviewMiAsserts.*;
import static xyz.jhmapstruct.domain.ReviewMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReviewMiMapperTest {

    private ReviewMiMapper reviewMiMapper;

    @BeforeEach
    void setUp() {
        reviewMiMapper = new ReviewMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReviewMiSample1();
        var actual = reviewMiMapper.toEntity(reviewMiMapper.toDto(expected));
        assertReviewMiAllPropertiesEquals(expected, actual);
    }
}

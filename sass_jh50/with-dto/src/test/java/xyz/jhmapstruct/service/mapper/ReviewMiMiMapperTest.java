package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ReviewMiMiAsserts.*;
import static xyz.jhmapstruct.domain.ReviewMiMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReviewMiMiMapperTest {

    private ReviewMiMiMapper reviewMiMiMapper;

    @BeforeEach
    void setUp() {
        reviewMiMiMapper = new ReviewMiMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReviewMiMiSample1();
        var actual = reviewMiMiMapper.toEntity(reviewMiMiMapper.toDto(expected));
        assertReviewMiMiAllPropertiesEquals(expected, actual);
    }
}

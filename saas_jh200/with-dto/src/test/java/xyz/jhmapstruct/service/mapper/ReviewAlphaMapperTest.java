package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ReviewAlphaAsserts.*;
import static xyz.jhmapstruct.domain.ReviewAlphaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReviewAlphaMapperTest {

    private ReviewAlphaMapper reviewAlphaMapper;

    @BeforeEach
    void setUp() {
        reviewAlphaMapper = new ReviewAlphaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReviewAlphaSample1();
        var actual = reviewAlphaMapper.toEntity(reviewAlphaMapper.toDto(expected));
        assertReviewAlphaAllPropertiesEquals(expected, actual);
    }
}

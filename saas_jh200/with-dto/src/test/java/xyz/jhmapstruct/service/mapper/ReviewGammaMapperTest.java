package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ReviewGammaAsserts.*;
import static xyz.jhmapstruct.domain.ReviewGammaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReviewGammaMapperTest {

    private ReviewGammaMapper reviewGammaMapper;

    @BeforeEach
    void setUp() {
        reviewGammaMapper = new ReviewGammaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReviewGammaSample1();
        var actual = reviewGammaMapper.toEntity(reviewGammaMapper.toDto(expected));
        assertReviewGammaAllPropertiesEquals(expected, actual);
    }
}

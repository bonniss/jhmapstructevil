package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextReviewGammaAsserts.*;
import static xyz.jhmapstruct.domain.NextReviewGammaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextReviewGammaMapperTest {

    private NextReviewGammaMapper nextReviewGammaMapper;

    @BeforeEach
    void setUp() {
        nextReviewGammaMapper = new NextReviewGammaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextReviewGammaSample1();
        var actual = nextReviewGammaMapper.toEntity(nextReviewGammaMapper.toDto(expected));
        assertNextReviewGammaAllPropertiesEquals(expected, actual);
    }
}

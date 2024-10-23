package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextReviewBetaAsserts.*;
import static xyz.jhmapstruct.domain.NextReviewBetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextReviewBetaMapperTest {

    private NextReviewBetaMapper nextReviewBetaMapper;

    @BeforeEach
    void setUp() {
        nextReviewBetaMapper = new NextReviewBetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextReviewBetaSample1();
        var actual = nextReviewBetaMapper.toEntity(nextReviewBetaMapper.toDto(expected));
        assertNextReviewBetaAllPropertiesEquals(expected, actual);
    }
}

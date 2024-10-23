package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextReviewSigmaAsserts.*;
import static xyz.jhmapstruct.domain.NextReviewSigmaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextReviewSigmaMapperTest {

    private NextReviewSigmaMapper nextReviewSigmaMapper;

    @BeforeEach
    void setUp() {
        nextReviewSigmaMapper = new NextReviewSigmaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextReviewSigmaSample1();
        var actual = nextReviewSigmaMapper.toEntity(nextReviewSigmaMapper.toDto(expected));
        assertNextReviewSigmaAllPropertiesEquals(expected, actual);
    }
}

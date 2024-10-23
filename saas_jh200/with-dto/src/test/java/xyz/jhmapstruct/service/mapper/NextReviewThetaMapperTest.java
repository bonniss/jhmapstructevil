package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextReviewThetaAsserts.*;
import static xyz.jhmapstruct.domain.NextReviewThetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextReviewThetaMapperTest {

    private NextReviewThetaMapper nextReviewThetaMapper;

    @BeforeEach
    void setUp() {
        nextReviewThetaMapper = new NextReviewThetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextReviewThetaSample1();
        var actual = nextReviewThetaMapper.toEntity(nextReviewThetaMapper.toDto(expected));
        assertNextReviewThetaAllPropertiesEquals(expected, actual);
    }
}

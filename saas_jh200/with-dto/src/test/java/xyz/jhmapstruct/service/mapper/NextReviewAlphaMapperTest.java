package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextReviewAlphaAsserts.*;
import static xyz.jhmapstruct.domain.NextReviewAlphaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextReviewAlphaMapperTest {

    private NextReviewAlphaMapper nextReviewAlphaMapper;

    @BeforeEach
    void setUp() {
        nextReviewAlphaMapper = new NextReviewAlphaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextReviewAlphaSample1();
        var actual = nextReviewAlphaMapper.toEntity(nextReviewAlphaMapper.toDto(expected));
        assertNextReviewAlphaAllPropertiesEquals(expected, actual);
    }
}

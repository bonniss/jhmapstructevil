package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextReviewMiMiAsserts.*;
import static xyz.jhmapstruct.domain.NextReviewMiMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextReviewMiMiMapperTest {

    private NextReviewMiMiMapper nextReviewMiMiMapper;

    @BeforeEach
    void setUp() {
        nextReviewMiMiMapper = new NextReviewMiMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextReviewMiMiSample1();
        var actual = nextReviewMiMiMapper.toEntity(nextReviewMiMiMapper.toDto(expected));
        assertNextReviewMiMiAllPropertiesEquals(expected, actual);
    }
}

package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextReviewMiAsserts.*;
import static xyz.jhmapstruct.domain.NextReviewMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextReviewMiMapperTest {

    private NextReviewMiMapper nextReviewMiMapper;

    @BeforeEach
    void setUp() {
        nextReviewMiMapper = new NextReviewMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextReviewMiSample1();
        var actual = nextReviewMiMapper.toEntity(nextReviewMiMapper.toDto(expected));
        assertNextReviewMiAllPropertiesEquals(expected, actual);
    }
}

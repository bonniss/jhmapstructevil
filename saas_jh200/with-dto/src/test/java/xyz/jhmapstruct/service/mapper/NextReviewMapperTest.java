package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextReviewAsserts.*;
import static xyz.jhmapstruct.domain.NextReviewTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextReviewMapperTest {

    private NextReviewMapper nextReviewMapper;

    @BeforeEach
    void setUp() {
        nextReviewMapper = new NextReviewMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextReviewSample1();
        var actual = nextReviewMapper.toEntity(nextReviewMapper.toDto(expected));
        assertNextReviewAllPropertiesEquals(expected, actual);
    }
}

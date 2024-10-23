package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextReviewViAsserts.*;
import static xyz.jhmapstruct.domain.NextReviewViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextReviewViMapperTest {

    private NextReviewViMapper nextReviewViMapper;

    @BeforeEach
    void setUp() {
        nextReviewViMapper = new NextReviewViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextReviewViSample1();
        var actual = nextReviewViMapper.toEntity(nextReviewViMapper.toDto(expected));
        assertNextReviewViAllPropertiesEquals(expected, actual);
    }
}

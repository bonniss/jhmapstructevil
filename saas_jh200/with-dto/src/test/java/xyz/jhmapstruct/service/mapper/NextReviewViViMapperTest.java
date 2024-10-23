package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextReviewViViAsserts.*;
import static xyz.jhmapstruct.domain.NextReviewViViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextReviewViViMapperTest {

    private NextReviewViViMapper nextReviewViViMapper;

    @BeforeEach
    void setUp() {
        nextReviewViViMapper = new NextReviewViViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextReviewViViSample1();
        var actual = nextReviewViViMapper.toEntity(nextReviewViViMapper.toDto(expected));
        assertNextReviewViViAllPropertiesEquals(expected, actual);
    }
}

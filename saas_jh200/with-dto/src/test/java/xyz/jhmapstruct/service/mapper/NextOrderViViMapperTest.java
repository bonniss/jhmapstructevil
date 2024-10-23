package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextOrderViViAsserts.*;
import static xyz.jhmapstruct.domain.NextOrderViViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextOrderViViMapperTest {

    private NextOrderViViMapper nextOrderViViMapper;

    @BeforeEach
    void setUp() {
        nextOrderViViMapper = new NextOrderViViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextOrderViViSample1();
        var actual = nextOrderViViMapper.toEntity(nextOrderViViMapper.toDto(expected));
        assertNextOrderViViAllPropertiesEquals(expected, actual);
    }
}

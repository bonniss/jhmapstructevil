package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextOrderViAsserts.*;
import static xyz.jhmapstruct.domain.NextOrderViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextOrderViMapperTest {

    private NextOrderViMapper nextOrderViMapper;

    @BeforeEach
    void setUp() {
        nextOrderViMapper = new NextOrderViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextOrderViSample1();
        var actual = nextOrderViMapper.toEntity(nextOrderViMapper.toDto(expected));
        assertNextOrderViAllPropertiesEquals(expected, actual);
    }
}

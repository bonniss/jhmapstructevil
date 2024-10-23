package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextProductViViAsserts.*;
import static xyz.jhmapstruct.domain.NextProductViViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextProductViViMapperTest {

    private NextProductViViMapper nextProductViViMapper;

    @BeforeEach
    void setUp() {
        nextProductViViMapper = new NextProductViViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextProductViViSample1();
        var actual = nextProductViViMapper.toEntity(nextProductViViMapper.toDto(expected));
        assertNextProductViViAllPropertiesEquals(expected, actual);
    }
}

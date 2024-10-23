package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextProductViAsserts.*;
import static xyz.jhmapstruct.domain.NextProductViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextProductViMapperTest {

    private NextProductViMapper nextProductViMapper;

    @BeforeEach
    void setUp() {
        nextProductViMapper = new NextProductViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextProductViSample1();
        var actual = nextProductViMapper.toEntity(nextProductViMapper.toDto(expected));
        assertNextProductViAllPropertiesEquals(expected, actual);
    }
}

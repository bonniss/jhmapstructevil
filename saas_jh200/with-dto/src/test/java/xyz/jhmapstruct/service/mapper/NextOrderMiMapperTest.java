package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextOrderMiAsserts.*;
import static xyz.jhmapstruct.domain.NextOrderMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextOrderMiMapperTest {

    private NextOrderMiMapper nextOrderMiMapper;

    @BeforeEach
    void setUp() {
        nextOrderMiMapper = new NextOrderMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextOrderMiSample1();
        var actual = nextOrderMiMapper.toEntity(nextOrderMiMapper.toDto(expected));
        assertNextOrderMiAllPropertiesEquals(expected, actual);
    }
}

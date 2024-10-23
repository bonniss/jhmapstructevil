package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextOrderMiMiAsserts.*;
import static xyz.jhmapstruct.domain.NextOrderMiMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextOrderMiMiMapperTest {

    private NextOrderMiMiMapper nextOrderMiMiMapper;

    @BeforeEach
    void setUp() {
        nextOrderMiMiMapper = new NextOrderMiMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextOrderMiMiSample1();
        var actual = nextOrderMiMiMapper.toEntity(nextOrderMiMiMapper.toDto(expected));
        assertNextOrderMiMiAllPropertiesEquals(expected, actual);
    }
}

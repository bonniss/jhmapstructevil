package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextOrderAsserts.*;
import static xyz.jhmapstruct.domain.NextOrderTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextOrderMapperTest {

    private NextOrderMapper nextOrderMapper;

    @BeforeEach
    void setUp() {
        nextOrderMapper = new NextOrderMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextOrderSample1();
        var actual = nextOrderMapper.toEntity(nextOrderMapper.toDto(expected));
        assertNextOrderAllPropertiesEquals(expected, actual);
    }
}

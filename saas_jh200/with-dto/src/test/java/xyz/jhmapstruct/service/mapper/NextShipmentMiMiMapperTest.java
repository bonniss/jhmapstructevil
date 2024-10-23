package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextShipmentMiMiAsserts.*;
import static xyz.jhmapstruct.domain.NextShipmentMiMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextShipmentMiMiMapperTest {

    private NextShipmentMiMiMapper nextShipmentMiMiMapper;

    @BeforeEach
    void setUp() {
        nextShipmentMiMiMapper = new NextShipmentMiMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextShipmentMiMiSample1();
        var actual = nextShipmentMiMiMapper.toEntity(nextShipmentMiMiMapper.toDto(expected));
        assertNextShipmentMiMiAllPropertiesEquals(expected, actual);
    }
}

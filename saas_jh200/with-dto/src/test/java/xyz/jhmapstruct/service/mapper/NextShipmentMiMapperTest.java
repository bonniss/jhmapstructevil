package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextShipmentMiAsserts.*;
import static xyz.jhmapstruct.domain.NextShipmentMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextShipmentMiMapperTest {

    private NextShipmentMiMapper nextShipmentMiMapper;

    @BeforeEach
    void setUp() {
        nextShipmentMiMapper = new NextShipmentMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextShipmentMiSample1();
        var actual = nextShipmentMiMapper.toEntity(nextShipmentMiMapper.toDto(expected));
        assertNextShipmentMiAllPropertiesEquals(expected, actual);
    }
}

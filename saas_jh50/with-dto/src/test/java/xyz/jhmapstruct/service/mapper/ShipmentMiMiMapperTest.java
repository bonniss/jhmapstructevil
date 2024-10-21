package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ShipmentMiMiAsserts.*;
import static xyz.jhmapstruct.domain.ShipmentMiMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShipmentMiMiMapperTest {

    private ShipmentMiMiMapper shipmentMiMiMapper;

    @BeforeEach
    void setUp() {
        shipmentMiMiMapper = new ShipmentMiMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getShipmentMiMiSample1();
        var actual = shipmentMiMiMapper.toEntity(shipmentMiMiMapper.toDto(expected));
        assertShipmentMiMiAllPropertiesEquals(expected, actual);
    }
}

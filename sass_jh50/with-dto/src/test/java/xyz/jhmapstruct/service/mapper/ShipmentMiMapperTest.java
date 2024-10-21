package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ShipmentMiAsserts.*;
import static xyz.jhmapstruct.domain.ShipmentMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShipmentMiMapperTest {

    private ShipmentMiMapper shipmentMiMapper;

    @BeforeEach
    void setUp() {
        shipmentMiMapper = new ShipmentMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getShipmentMiSample1();
        var actual = shipmentMiMapper.toEntity(shipmentMiMapper.toDto(expected));
        assertShipmentMiAllPropertiesEquals(expected, actual);
    }
}

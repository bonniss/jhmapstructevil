package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ShipmentViAsserts.*;
import static xyz.jhmapstruct.domain.ShipmentViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShipmentViMapperTest {

    private ShipmentViMapper shipmentViMapper;

    @BeforeEach
    void setUp() {
        shipmentViMapper = new ShipmentViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getShipmentViSample1();
        var actual = shipmentViMapper.toEntity(shipmentViMapper.toDto(expected));
        assertShipmentViAllPropertiesEquals(expected, actual);
    }
}

package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ShipmentViViAsserts.*;
import static xyz.jhmapstruct.domain.ShipmentViViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShipmentViViMapperTest {

    private ShipmentViViMapper shipmentViViMapper;

    @BeforeEach
    void setUp() {
        shipmentViViMapper = new ShipmentViViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getShipmentViViSample1();
        var actual = shipmentViViMapper.toEntity(shipmentViViMapper.toDto(expected));
        assertShipmentViViAllPropertiesEquals(expected, actual);
    }
}

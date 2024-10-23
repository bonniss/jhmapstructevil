package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ShipmentAlphaAsserts.*;
import static xyz.jhmapstruct.domain.ShipmentAlphaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShipmentAlphaMapperTest {

    private ShipmentAlphaMapper shipmentAlphaMapper;

    @BeforeEach
    void setUp() {
        shipmentAlphaMapper = new ShipmentAlphaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getShipmentAlphaSample1();
        var actual = shipmentAlphaMapper.toEntity(shipmentAlphaMapper.toDto(expected));
        assertShipmentAlphaAllPropertiesEquals(expected, actual);
    }
}

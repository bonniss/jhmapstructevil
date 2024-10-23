package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ShipmentSigmaAsserts.*;
import static xyz.jhmapstruct.domain.ShipmentSigmaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShipmentSigmaMapperTest {

    private ShipmentSigmaMapper shipmentSigmaMapper;

    @BeforeEach
    void setUp() {
        shipmentSigmaMapper = new ShipmentSigmaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getShipmentSigmaSample1();
        var actual = shipmentSigmaMapper.toEntity(shipmentSigmaMapper.toDto(expected));
        assertShipmentSigmaAllPropertiesEquals(expected, actual);
    }
}

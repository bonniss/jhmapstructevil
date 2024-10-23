package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ShipmentGammaAsserts.*;
import static xyz.jhmapstruct.domain.ShipmentGammaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShipmentGammaMapperTest {

    private ShipmentGammaMapper shipmentGammaMapper;

    @BeforeEach
    void setUp() {
        shipmentGammaMapper = new ShipmentGammaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getShipmentGammaSample1();
        var actual = shipmentGammaMapper.toEntity(shipmentGammaMapper.toDto(expected));
        assertShipmentGammaAllPropertiesEquals(expected, actual);
    }
}

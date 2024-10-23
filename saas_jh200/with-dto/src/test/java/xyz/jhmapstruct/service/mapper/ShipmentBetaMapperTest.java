package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ShipmentBetaAsserts.*;
import static xyz.jhmapstruct.domain.ShipmentBetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShipmentBetaMapperTest {

    private ShipmentBetaMapper shipmentBetaMapper;

    @BeforeEach
    void setUp() {
        shipmentBetaMapper = new ShipmentBetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getShipmentBetaSample1();
        var actual = shipmentBetaMapper.toEntity(shipmentBetaMapper.toDto(expected));
        assertShipmentBetaAllPropertiesEquals(expected, actual);
    }
}

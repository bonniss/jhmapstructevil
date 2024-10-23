package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ShipmentThetaAsserts.*;
import static xyz.jhmapstruct.domain.ShipmentThetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShipmentThetaMapperTest {

    private ShipmentThetaMapper shipmentThetaMapper;

    @BeforeEach
    void setUp() {
        shipmentThetaMapper = new ShipmentThetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getShipmentThetaSample1();
        var actual = shipmentThetaMapper.toEntity(shipmentThetaMapper.toDto(expected));
        assertShipmentThetaAllPropertiesEquals(expected, actual);
    }
}

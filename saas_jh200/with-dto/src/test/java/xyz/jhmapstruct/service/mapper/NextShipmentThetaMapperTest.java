package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextShipmentThetaAsserts.*;
import static xyz.jhmapstruct.domain.NextShipmentThetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextShipmentThetaMapperTest {

    private NextShipmentThetaMapper nextShipmentThetaMapper;

    @BeforeEach
    void setUp() {
        nextShipmentThetaMapper = new NextShipmentThetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextShipmentThetaSample1();
        var actual = nextShipmentThetaMapper.toEntity(nextShipmentThetaMapper.toDto(expected));
        assertNextShipmentThetaAllPropertiesEquals(expected, actual);
    }
}

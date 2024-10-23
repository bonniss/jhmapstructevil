package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextShipmentAlphaAsserts.*;
import static xyz.jhmapstruct.domain.NextShipmentAlphaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextShipmentAlphaMapperTest {

    private NextShipmentAlphaMapper nextShipmentAlphaMapper;

    @BeforeEach
    void setUp() {
        nextShipmentAlphaMapper = new NextShipmentAlphaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextShipmentAlphaSample1();
        var actual = nextShipmentAlphaMapper.toEntity(nextShipmentAlphaMapper.toDto(expected));
        assertNextShipmentAlphaAllPropertiesEquals(expected, actual);
    }
}

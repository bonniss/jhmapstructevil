package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextShipmentSigmaAsserts.*;
import static xyz.jhmapstruct.domain.NextShipmentSigmaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextShipmentSigmaMapperTest {

    private NextShipmentSigmaMapper nextShipmentSigmaMapper;

    @BeforeEach
    void setUp() {
        nextShipmentSigmaMapper = new NextShipmentSigmaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextShipmentSigmaSample1();
        var actual = nextShipmentSigmaMapper.toEntity(nextShipmentSigmaMapper.toDto(expected));
        assertNextShipmentSigmaAllPropertiesEquals(expected, actual);
    }
}

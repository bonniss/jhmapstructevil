package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextShipmentAsserts.*;
import static xyz.jhmapstruct.domain.NextShipmentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextShipmentMapperTest {

    private NextShipmentMapper nextShipmentMapper;

    @BeforeEach
    void setUp() {
        nextShipmentMapper = new NextShipmentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextShipmentSample1();
        var actual = nextShipmentMapper.toEntity(nextShipmentMapper.toDto(expected));
        assertNextShipmentAllPropertiesEquals(expected, actual);
    }
}

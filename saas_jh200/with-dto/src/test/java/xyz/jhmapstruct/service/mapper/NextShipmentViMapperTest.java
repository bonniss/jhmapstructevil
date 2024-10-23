package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextShipmentViAsserts.*;
import static xyz.jhmapstruct.domain.NextShipmentViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextShipmentViMapperTest {

    private NextShipmentViMapper nextShipmentViMapper;

    @BeforeEach
    void setUp() {
        nextShipmentViMapper = new NextShipmentViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextShipmentViSample1();
        var actual = nextShipmentViMapper.toEntity(nextShipmentViMapper.toDto(expected));
        assertNextShipmentViAllPropertiesEquals(expected, actual);
    }
}

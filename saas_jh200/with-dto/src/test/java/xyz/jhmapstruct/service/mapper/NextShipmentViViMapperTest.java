package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextShipmentViViAsserts.*;
import static xyz.jhmapstruct.domain.NextShipmentViViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextShipmentViViMapperTest {

    private NextShipmentViViMapper nextShipmentViViMapper;

    @BeforeEach
    void setUp() {
        nextShipmentViViMapper = new NextShipmentViViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextShipmentViViSample1();
        var actual = nextShipmentViViMapper.toEntity(nextShipmentViViMapper.toDto(expected));
        assertNextShipmentViViAllPropertiesEquals(expected, actual);
    }
}

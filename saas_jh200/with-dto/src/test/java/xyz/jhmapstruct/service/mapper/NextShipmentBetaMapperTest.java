package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextShipmentBetaAsserts.*;
import static xyz.jhmapstruct.domain.NextShipmentBetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextShipmentBetaMapperTest {

    private NextShipmentBetaMapper nextShipmentBetaMapper;

    @BeforeEach
    void setUp() {
        nextShipmentBetaMapper = new NextShipmentBetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextShipmentBetaSample1();
        var actual = nextShipmentBetaMapper.toEntity(nextShipmentBetaMapper.toDto(expected));
        assertNextShipmentBetaAllPropertiesEquals(expected, actual);
    }
}

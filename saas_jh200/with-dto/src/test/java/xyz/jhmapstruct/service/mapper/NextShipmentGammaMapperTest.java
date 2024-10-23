package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextShipmentGammaAsserts.*;
import static xyz.jhmapstruct.domain.NextShipmentGammaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextShipmentGammaMapperTest {

    private NextShipmentGammaMapper nextShipmentGammaMapper;

    @BeforeEach
    void setUp() {
        nextShipmentGammaMapper = new NextShipmentGammaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextShipmentGammaSample1();
        var actual = nextShipmentGammaMapper.toEntity(nextShipmentGammaMapper.toDto(expected));
        assertNextShipmentGammaAllPropertiesEquals(expected, actual);
    }
}

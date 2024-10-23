package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextEmployeeGammaAsserts.*;
import static xyz.jhmapstruct.domain.NextEmployeeGammaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextEmployeeGammaMapperTest {

    private NextEmployeeGammaMapper nextEmployeeGammaMapper;

    @BeforeEach
    void setUp() {
        nextEmployeeGammaMapper = new NextEmployeeGammaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextEmployeeGammaSample1();
        var actual = nextEmployeeGammaMapper.toEntity(nextEmployeeGammaMapper.toDto(expected));
        assertNextEmployeeGammaAllPropertiesEquals(expected, actual);
    }
}

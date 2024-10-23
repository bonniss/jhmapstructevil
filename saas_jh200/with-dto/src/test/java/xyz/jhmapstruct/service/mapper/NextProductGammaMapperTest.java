package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextProductGammaAsserts.*;
import static xyz.jhmapstruct.domain.NextProductGammaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextProductGammaMapperTest {

    private NextProductGammaMapper nextProductGammaMapper;

    @BeforeEach
    void setUp() {
        nextProductGammaMapper = new NextProductGammaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextProductGammaSample1();
        var actual = nextProductGammaMapper.toEntity(nextProductGammaMapper.toDto(expected));
        assertNextProductGammaAllPropertiesEquals(expected, actual);
    }
}

package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextOrderGammaAsserts.*;
import static xyz.jhmapstruct.domain.NextOrderGammaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextOrderGammaMapperTest {

    private NextOrderGammaMapper nextOrderGammaMapper;

    @BeforeEach
    void setUp() {
        nextOrderGammaMapper = new NextOrderGammaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextOrderGammaSample1();
        var actual = nextOrderGammaMapper.toEntity(nextOrderGammaMapper.toDto(expected));
        assertNextOrderGammaAllPropertiesEquals(expected, actual);
    }
}

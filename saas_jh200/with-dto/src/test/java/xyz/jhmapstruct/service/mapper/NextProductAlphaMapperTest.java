package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextProductAlphaAsserts.*;
import static xyz.jhmapstruct.domain.NextProductAlphaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextProductAlphaMapperTest {

    private NextProductAlphaMapper nextProductAlphaMapper;

    @BeforeEach
    void setUp() {
        nextProductAlphaMapper = new NextProductAlphaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextProductAlphaSample1();
        var actual = nextProductAlphaMapper.toEntity(nextProductAlphaMapper.toDto(expected));
        assertNextProductAlphaAllPropertiesEquals(expected, actual);
    }
}

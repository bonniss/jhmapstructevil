package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextEmployeeAlphaAsserts.*;
import static xyz.jhmapstruct.domain.NextEmployeeAlphaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextEmployeeAlphaMapperTest {

    private NextEmployeeAlphaMapper nextEmployeeAlphaMapper;

    @BeforeEach
    void setUp() {
        nextEmployeeAlphaMapper = new NextEmployeeAlphaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextEmployeeAlphaSample1();
        var actual = nextEmployeeAlphaMapper.toEntity(nextEmployeeAlphaMapper.toDto(expected));
        assertNextEmployeeAlphaAllPropertiesEquals(expected, actual);
    }
}

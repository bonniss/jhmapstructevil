package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextEmployeeSigmaAsserts.*;
import static xyz.jhmapstruct.domain.NextEmployeeSigmaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextEmployeeSigmaMapperTest {

    private NextEmployeeSigmaMapper nextEmployeeSigmaMapper;

    @BeforeEach
    void setUp() {
        nextEmployeeSigmaMapper = new NextEmployeeSigmaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextEmployeeSigmaSample1();
        var actual = nextEmployeeSigmaMapper.toEntity(nextEmployeeSigmaMapper.toDto(expected));
        assertNextEmployeeSigmaAllPropertiesEquals(expected, actual);
    }
}

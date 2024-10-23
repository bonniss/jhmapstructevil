package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextEmployeeThetaAsserts.*;
import static xyz.jhmapstruct.domain.NextEmployeeThetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextEmployeeThetaMapperTest {

    private NextEmployeeThetaMapper nextEmployeeThetaMapper;

    @BeforeEach
    void setUp() {
        nextEmployeeThetaMapper = new NextEmployeeThetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextEmployeeThetaSample1();
        var actual = nextEmployeeThetaMapper.toEntity(nextEmployeeThetaMapper.toDto(expected));
        assertNextEmployeeThetaAllPropertiesEquals(expected, actual);
    }
}

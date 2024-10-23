package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextEmployeeBetaAsserts.*;
import static xyz.jhmapstruct.domain.NextEmployeeBetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextEmployeeBetaMapperTest {

    private NextEmployeeBetaMapper nextEmployeeBetaMapper;

    @BeforeEach
    void setUp() {
        nextEmployeeBetaMapper = new NextEmployeeBetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextEmployeeBetaSample1();
        var actual = nextEmployeeBetaMapper.toEntity(nextEmployeeBetaMapper.toDto(expected));
        assertNextEmployeeBetaAllPropertiesEquals(expected, actual);
    }
}

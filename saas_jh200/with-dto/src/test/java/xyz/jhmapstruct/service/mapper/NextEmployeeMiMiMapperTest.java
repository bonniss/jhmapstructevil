package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextEmployeeMiMiAsserts.*;
import static xyz.jhmapstruct.domain.NextEmployeeMiMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextEmployeeMiMiMapperTest {

    private NextEmployeeMiMiMapper nextEmployeeMiMiMapper;

    @BeforeEach
    void setUp() {
        nextEmployeeMiMiMapper = new NextEmployeeMiMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextEmployeeMiMiSample1();
        var actual = nextEmployeeMiMiMapper.toEntity(nextEmployeeMiMiMapper.toDto(expected));
        assertNextEmployeeMiMiAllPropertiesEquals(expected, actual);
    }
}

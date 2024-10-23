package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextEmployeeMiAsserts.*;
import static xyz.jhmapstruct.domain.NextEmployeeMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextEmployeeMiMapperTest {

    private NextEmployeeMiMapper nextEmployeeMiMapper;

    @BeforeEach
    void setUp() {
        nextEmployeeMiMapper = new NextEmployeeMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextEmployeeMiSample1();
        var actual = nextEmployeeMiMapper.toEntity(nextEmployeeMiMapper.toDto(expected));
        assertNextEmployeeMiAllPropertiesEquals(expected, actual);
    }
}

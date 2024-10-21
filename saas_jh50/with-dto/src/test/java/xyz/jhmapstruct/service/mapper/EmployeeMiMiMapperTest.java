package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.EmployeeMiMiAsserts.*;
import static xyz.jhmapstruct.domain.EmployeeMiMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeMiMiMapperTest {

    private EmployeeMiMiMapper employeeMiMiMapper;

    @BeforeEach
    void setUp() {
        employeeMiMiMapper = new EmployeeMiMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEmployeeMiMiSample1();
        var actual = employeeMiMiMapper.toEntity(employeeMiMiMapper.toDto(expected));
        assertEmployeeMiMiAllPropertiesEquals(expected, actual);
    }
}

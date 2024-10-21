package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.EmployeeMiAsserts.*;
import static xyz.jhmapstruct.domain.EmployeeMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeMiMapperTest {

    private EmployeeMiMapper employeeMiMapper;

    @BeforeEach
    void setUp() {
        employeeMiMapper = new EmployeeMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEmployeeMiSample1();
        var actual = employeeMiMapper.toEntity(employeeMiMapper.toDto(expected));
        assertEmployeeMiAllPropertiesEquals(expected, actual);
    }
}

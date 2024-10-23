package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.EmployeeAlphaAsserts.*;
import static xyz.jhmapstruct.domain.EmployeeAlphaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeAlphaMapperTest {

    private EmployeeAlphaMapper employeeAlphaMapper;

    @BeforeEach
    void setUp() {
        employeeAlphaMapper = new EmployeeAlphaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEmployeeAlphaSample1();
        var actual = employeeAlphaMapper.toEntity(employeeAlphaMapper.toDto(expected));
        assertEmployeeAlphaAllPropertiesEquals(expected, actual);
    }
}

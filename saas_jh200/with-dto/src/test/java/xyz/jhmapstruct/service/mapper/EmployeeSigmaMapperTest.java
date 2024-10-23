package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.EmployeeSigmaAsserts.*;
import static xyz.jhmapstruct.domain.EmployeeSigmaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeSigmaMapperTest {

    private EmployeeSigmaMapper employeeSigmaMapper;

    @BeforeEach
    void setUp() {
        employeeSigmaMapper = new EmployeeSigmaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEmployeeSigmaSample1();
        var actual = employeeSigmaMapper.toEntity(employeeSigmaMapper.toDto(expected));
        assertEmployeeSigmaAllPropertiesEquals(expected, actual);
    }
}

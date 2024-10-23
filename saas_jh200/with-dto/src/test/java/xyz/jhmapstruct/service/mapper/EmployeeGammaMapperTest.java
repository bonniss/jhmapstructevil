package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.EmployeeGammaAsserts.*;
import static xyz.jhmapstruct.domain.EmployeeGammaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeGammaMapperTest {

    private EmployeeGammaMapper employeeGammaMapper;

    @BeforeEach
    void setUp() {
        employeeGammaMapper = new EmployeeGammaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEmployeeGammaSample1();
        var actual = employeeGammaMapper.toEntity(employeeGammaMapper.toDto(expected));
        assertEmployeeGammaAllPropertiesEquals(expected, actual);
    }
}

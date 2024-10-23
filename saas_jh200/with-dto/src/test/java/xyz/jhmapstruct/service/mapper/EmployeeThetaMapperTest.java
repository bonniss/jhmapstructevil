package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.EmployeeThetaAsserts.*;
import static xyz.jhmapstruct.domain.EmployeeThetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeThetaMapperTest {

    private EmployeeThetaMapper employeeThetaMapper;

    @BeforeEach
    void setUp() {
        employeeThetaMapper = new EmployeeThetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEmployeeThetaSample1();
        var actual = employeeThetaMapper.toEntity(employeeThetaMapper.toDto(expected));
        assertEmployeeThetaAllPropertiesEquals(expected, actual);
    }
}

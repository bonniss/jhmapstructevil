package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.EmployeeBetaAsserts.*;
import static xyz.jhmapstruct.domain.EmployeeBetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeBetaMapperTest {

    private EmployeeBetaMapper employeeBetaMapper;

    @BeforeEach
    void setUp() {
        employeeBetaMapper = new EmployeeBetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEmployeeBetaSample1();
        var actual = employeeBetaMapper.toEntity(employeeBetaMapper.toDto(expected));
        assertEmployeeBetaAllPropertiesEquals(expected, actual);
    }
}

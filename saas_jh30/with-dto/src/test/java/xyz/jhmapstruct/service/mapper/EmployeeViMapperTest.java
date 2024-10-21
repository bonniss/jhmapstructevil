package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.EmployeeViAsserts.*;
import static xyz.jhmapstruct.domain.EmployeeViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeViMapperTest {

    private EmployeeViMapper employeeViMapper;

    @BeforeEach
    void setUp() {
        employeeViMapper = new EmployeeViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEmployeeViSample1();
        var actual = employeeViMapper.toEntity(employeeViMapper.toDto(expected));
        assertEmployeeViAllPropertiesEquals(expected, actual);
    }
}

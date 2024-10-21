package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.EmployeeViViAsserts.*;
import static xyz.jhmapstruct.domain.EmployeeViViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeViViMapperTest {

    private EmployeeViViMapper employeeViViMapper;

    @BeforeEach
    void setUp() {
        employeeViViMapper = new EmployeeViViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEmployeeViViSample1();
        var actual = employeeViViMapper.toEntity(employeeViViMapper.toDto(expected));
        assertEmployeeViViAllPropertiesEquals(expected, actual);
    }
}

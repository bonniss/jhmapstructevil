package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextEmployeeAsserts.*;
import static xyz.jhmapstruct.domain.NextEmployeeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextEmployeeMapperTest {

    private NextEmployeeMapper nextEmployeeMapper;

    @BeforeEach
    void setUp() {
        nextEmployeeMapper = new NextEmployeeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextEmployeeSample1();
        var actual = nextEmployeeMapper.toEntity(nextEmployeeMapper.toDto(expected));
        assertNextEmployeeAllPropertiesEquals(expected, actual);
    }
}

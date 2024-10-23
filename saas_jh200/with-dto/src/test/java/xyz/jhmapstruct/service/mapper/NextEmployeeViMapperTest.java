package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextEmployeeViAsserts.*;
import static xyz.jhmapstruct.domain.NextEmployeeViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextEmployeeViMapperTest {

    private NextEmployeeViMapper nextEmployeeViMapper;

    @BeforeEach
    void setUp() {
        nextEmployeeViMapper = new NextEmployeeViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextEmployeeViSample1();
        var actual = nextEmployeeViMapper.toEntity(nextEmployeeViMapper.toDto(expected));
        assertNextEmployeeViAllPropertiesEquals(expected, actual);
    }
}

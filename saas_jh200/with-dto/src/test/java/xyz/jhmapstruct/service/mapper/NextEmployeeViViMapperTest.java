package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextEmployeeViViAsserts.*;
import static xyz.jhmapstruct.domain.NextEmployeeViViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextEmployeeViViMapperTest {

    private NextEmployeeViViMapper nextEmployeeViViMapper;

    @BeforeEach
    void setUp() {
        nextEmployeeViViMapper = new NextEmployeeViViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextEmployeeViViSample1();
        var actual = nextEmployeeViViMapper.toEntity(nextEmployeeViViMapper.toDto(expected));
        assertNextEmployeeViViAllPropertiesEquals(expected, actual);
    }
}

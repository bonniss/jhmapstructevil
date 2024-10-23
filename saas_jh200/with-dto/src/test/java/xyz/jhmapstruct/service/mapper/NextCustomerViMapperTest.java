package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextCustomerViAsserts.*;
import static xyz.jhmapstruct.domain.NextCustomerViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextCustomerViMapperTest {

    private NextCustomerViMapper nextCustomerViMapper;

    @BeforeEach
    void setUp() {
        nextCustomerViMapper = new NextCustomerViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextCustomerViSample1();
        var actual = nextCustomerViMapper.toEntity(nextCustomerViMapper.toDto(expected));
        assertNextCustomerViAllPropertiesEquals(expected, actual);
    }
}

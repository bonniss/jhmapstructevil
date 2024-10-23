package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextCustomerViViAsserts.*;
import static xyz.jhmapstruct.domain.NextCustomerViViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextCustomerViViMapperTest {

    private NextCustomerViViMapper nextCustomerViViMapper;

    @BeforeEach
    void setUp() {
        nextCustomerViViMapper = new NextCustomerViViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextCustomerViViSample1();
        var actual = nextCustomerViViMapper.toEntity(nextCustomerViViMapper.toDto(expected));
        assertNextCustomerViViAllPropertiesEquals(expected, actual);
    }
}

package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextCustomerMiAsserts.*;
import static xyz.jhmapstruct.domain.NextCustomerMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextCustomerMiMapperTest {

    private NextCustomerMiMapper nextCustomerMiMapper;

    @BeforeEach
    void setUp() {
        nextCustomerMiMapper = new NextCustomerMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextCustomerMiSample1();
        var actual = nextCustomerMiMapper.toEntity(nextCustomerMiMapper.toDto(expected));
        assertNextCustomerMiAllPropertiesEquals(expected, actual);
    }
}

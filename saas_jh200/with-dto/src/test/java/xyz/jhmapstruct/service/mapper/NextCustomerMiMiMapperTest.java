package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextCustomerMiMiAsserts.*;
import static xyz.jhmapstruct.domain.NextCustomerMiMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextCustomerMiMiMapperTest {

    private NextCustomerMiMiMapper nextCustomerMiMiMapper;

    @BeforeEach
    void setUp() {
        nextCustomerMiMiMapper = new NextCustomerMiMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextCustomerMiMiSample1();
        var actual = nextCustomerMiMiMapper.toEntity(nextCustomerMiMiMapper.toDto(expected));
        assertNextCustomerMiMiAllPropertiesEquals(expected, actual);
    }
}

package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextCustomerAsserts.*;
import static xyz.jhmapstruct.domain.NextCustomerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextCustomerMapperTest {

    private NextCustomerMapper nextCustomerMapper;

    @BeforeEach
    void setUp() {
        nextCustomerMapper = new NextCustomerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextCustomerSample1();
        var actual = nextCustomerMapper.toEntity(nextCustomerMapper.toDto(expected));
        assertNextCustomerAllPropertiesEquals(expected, actual);
    }
}

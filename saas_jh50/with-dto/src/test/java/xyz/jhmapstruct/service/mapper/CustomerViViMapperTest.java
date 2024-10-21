package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.CustomerViViAsserts.*;
import static xyz.jhmapstruct.domain.CustomerViViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerViViMapperTest {

    private CustomerViViMapper customerViViMapper;

    @BeforeEach
    void setUp() {
        customerViViMapper = new CustomerViViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCustomerViViSample1();
        var actual = customerViViMapper.toEntity(customerViViMapper.toDto(expected));
        assertCustomerViViAllPropertiesEquals(expected, actual);
    }
}

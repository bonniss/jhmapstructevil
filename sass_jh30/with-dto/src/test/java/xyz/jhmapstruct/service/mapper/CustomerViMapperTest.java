package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.CustomerViAsserts.*;
import static xyz.jhmapstruct.domain.CustomerViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerViMapperTest {

    private CustomerViMapper customerViMapper;

    @BeforeEach
    void setUp() {
        customerViMapper = new CustomerViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCustomerViSample1();
        var actual = customerViMapper.toEntity(customerViMapper.toDto(expected));
        assertCustomerViAllPropertiesEquals(expected, actual);
    }
}

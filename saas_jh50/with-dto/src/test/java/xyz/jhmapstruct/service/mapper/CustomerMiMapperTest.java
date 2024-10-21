package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.CustomerMiAsserts.*;
import static xyz.jhmapstruct.domain.CustomerMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerMiMapperTest {

    private CustomerMiMapper customerMiMapper;

    @BeforeEach
    void setUp() {
        customerMiMapper = new CustomerMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCustomerMiSample1();
        var actual = customerMiMapper.toEntity(customerMiMapper.toDto(expected));
        assertCustomerMiAllPropertiesEquals(expected, actual);
    }
}

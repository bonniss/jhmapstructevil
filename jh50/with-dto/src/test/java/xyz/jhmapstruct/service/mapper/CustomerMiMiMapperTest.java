package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.CustomerMiMiAsserts.*;
import static xyz.jhmapstruct.domain.CustomerMiMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerMiMiMapperTest {

    private CustomerMiMiMapper customerMiMiMapper;

    @BeforeEach
    void setUp() {
        customerMiMiMapper = new CustomerMiMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCustomerMiMiSample1();
        var actual = customerMiMiMapper.toEntity(customerMiMiMapper.toDto(expected));
        assertCustomerMiMiAllPropertiesEquals(expected, actual);
    }
}

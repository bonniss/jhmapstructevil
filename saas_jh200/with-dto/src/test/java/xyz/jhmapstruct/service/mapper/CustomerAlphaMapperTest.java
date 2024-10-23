package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.CustomerAlphaAsserts.*;
import static xyz.jhmapstruct.domain.CustomerAlphaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerAlphaMapperTest {

    private CustomerAlphaMapper customerAlphaMapper;

    @BeforeEach
    void setUp() {
        customerAlphaMapper = new CustomerAlphaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCustomerAlphaSample1();
        var actual = customerAlphaMapper.toEntity(customerAlphaMapper.toDto(expected));
        assertCustomerAlphaAllPropertiesEquals(expected, actual);
    }
}

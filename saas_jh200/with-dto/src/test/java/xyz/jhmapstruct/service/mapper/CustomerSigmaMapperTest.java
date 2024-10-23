package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.CustomerSigmaAsserts.*;
import static xyz.jhmapstruct.domain.CustomerSigmaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerSigmaMapperTest {

    private CustomerSigmaMapper customerSigmaMapper;

    @BeforeEach
    void setUp() {
        customerSigmaMapper = new CustomerSigmaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCustomerSigmaSample1();
        var actual = customerSigmaMapper.toEntity(customerSigmaMapper.toDto(expected));
        assertCustomerSigmaAllPropertiesEquals(expected, actual);
    }
}

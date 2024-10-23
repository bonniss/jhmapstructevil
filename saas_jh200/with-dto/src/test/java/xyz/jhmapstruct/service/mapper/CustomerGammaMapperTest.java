package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.CustomerGammaAsserts.*;
import static xyz.jhmapstruct.domain.CustomerGammaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerGammaMapperTest {

    private CustomerGammaMapper customerGammaMapper;

    @BeforeEach
    void setUp() {
        customerGammaMapper = new CustomerGammaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCustomerGammaSample1();
        var actual = customerGammaMapper.toEntity(customerGammaMapper.toDto(expected));
        assertCustomerGammaAllPropertiesEquals(expected, actual);
    }
}

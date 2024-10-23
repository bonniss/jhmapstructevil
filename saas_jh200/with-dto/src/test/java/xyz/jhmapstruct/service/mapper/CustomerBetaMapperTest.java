package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.CustomerBetaAsserts.*;
import static xyz.jhmapstruct.domain.CustomerBetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerBetaMapperTest {

    private CustomerBetaMapper customerBetaMapper;

    @BeforeEach
    void setUp() {
        customerBetaMapper = new CustomerBetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCustomerBetaSample1();
        var actual = customerBetaMapper.toEntity(customerBetaMapper.toDto(expected));
        assertCustomerBetaAllPropertiesEquals(expected, actual);
    }
}

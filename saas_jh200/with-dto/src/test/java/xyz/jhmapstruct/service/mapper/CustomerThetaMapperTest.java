package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.CustomerThetaAsserts.*;
import static xyz.jhmapstruct.domain.CustomerThetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerThetaMapperTest {

    private CustomerThetaMapper customerThetaMapper;

    @BeforeEach
    void setUp() {
        customerThetaMapper = new CustomerThetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCustomerThetaSample1();
        var actual = customerThetaMapper.toEntity(customerThetaMapper.toDto(expected));
        assertCustomerThetaAllPropertiesEquals(expected, actual);
    }
}

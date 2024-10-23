package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.OrderSigmaAsserts.*;
import static xyz.jhmapstruct.domain.OrderSigmaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderSigmaMapperTest {

    private OrderSigmaMapper orderSigmaMapper;

    @BeforeEach
    void setUp() {
        orderSigmaMapper = new OrderSigmaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOrderSigmaSample1();
        var actual = orderSigmaMapper.toEntity(orderSigmaMapper.toDto(expected));
        assertOrderSigmaAllPropertiesEquals(expected, actual);
    }
}

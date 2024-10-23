package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.OrderGammaAsserts.*;
import static xyz.jhmapstruct.domain.OrderGammaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderGammaMapperTest {

    private OrderGammaMapper orderGammaMapper;

    @BeforeEach
    void setUp() {
        orderGammaMapper = new OrderGammaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOrderGammaSample1();
        var actual = orderGammaMapper.toEntity(orderGammaMapper.toDto(expected));
        assertOrderGammaAllPropertiesEquals(expected, actual);
    }
}

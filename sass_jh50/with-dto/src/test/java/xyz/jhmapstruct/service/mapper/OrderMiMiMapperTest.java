package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.OrderMiMiAsserts.*;
import static xyz.jhmapstruct.domain.OrderMiMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderMiMiMapperTest {

    private OrderMiMiMapper orderMiMiMapper;

    @BeforeEach
    void setUp() {
        orderMiMiMapper = new OrderMiMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOrderMiMiSample1();
        var actual = orderMiMiMapper.toEntity(orderMiMiMapper.toDto(expected));
        assertOrderMiMiAllPropertiesEquals(expected, actual);
    }
}

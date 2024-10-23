package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.OrderMiAsserts.*;
import static xyz.jhmapstruct.domain.OrderMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderMiMapperTest {

    private OrderMiMapper orderMiMapper;

    @BeforeEach
    void setUp() {
        orderMiMapper = new OrderMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOrderMiSample1();
        var actual = orderMiMapper.toEntity(orderMiMapper.toDto(expected));
        assertOrderMiAllPropertiesEquals(expected, actual);
    }
}

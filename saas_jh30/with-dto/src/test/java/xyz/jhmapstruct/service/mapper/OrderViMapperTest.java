package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.OrderViAsserts.*;
import static xyz.jhmapstruct.domain.OrderViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderViMapperTest {

    private OrderViMapper orderViMapper;

    @BeforeEach
    void setUp() {
        orderViMapper = new OrderViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOrderViSample1();
        var actual = orderViMapper.toEntity(orderViMapper.toDto(expected));
        assertOrderViAllPropertiesEquals(expected, actual);
    }
}

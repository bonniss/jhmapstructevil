package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.OrderViViAsserts.*;
import static xyz.jhmapstruct.domain.OrderViViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderViViMapperTest {

    private OrderViViMapper orderViViMapper;

    @BeforeEach
    void setUp() {
        orderViViMapper = new OrderViViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOrderViViSample1();
        var actual = orderViViMapper.toEntity(orderViViMapper.toDto(expected));
        assertOrderViViAllPropertiesEquals(expected, actual);
    }
}

package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.OrderAlphaAsserts.*;
import static xyz.jhmapstruct.domain.OrderAlphaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderAlphaMapperTest {

    private OrderAlphaMapper orderAlphaMapper;

    @BeforeEach
    void setUp() {
        orderAlphaMapper = new OrderAlphaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOrderAlphaSample1();
        var actual = orderAlphaMapper.toEntity(orderAlphaMapper.toDto(expected));
        assertOrderAlphaAllPropertiesEquals(expected, actual);
    }
}

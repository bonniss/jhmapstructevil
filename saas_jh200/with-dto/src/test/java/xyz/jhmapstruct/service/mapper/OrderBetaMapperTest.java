package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.OrderBetaAsserts.*;
import static xyz.jhmapstruct.domain.OrderBetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderBetaMapperTest {

    private OrderBetaMapper orderBetaMapper;

    @BeforeEach
    void setUp() {
        orderBetaMapper = new OrderBetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOrderBetaSample1();
        var actual = orderBetaMapper.toEntity(orderBetaMapper.toDto(expected));
        assertOrderBetaAllPropertiesEquals(expected, actual);
    }
}

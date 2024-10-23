package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.OrderThetaAsserts.*;
import static xyz.jhmapstruct.domain.OrderThetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderThetaMapperTest {

    private OrderThetaMapper orderThetaMapper;

    @BeforeEach
    void setUp() {
        orderThetaMapper = new OrderThetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOrderThetaSample1();
        var actual = orderThetaMapper.toEntity(orderThetaMapper.toDto(expected));
        assertOrderThetaAllPropertiesEquals(expected, actual);
    }
}

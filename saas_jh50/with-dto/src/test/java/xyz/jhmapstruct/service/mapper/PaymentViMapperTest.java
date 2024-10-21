package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.PaymentViAsserts.*;
import static xyz.jhmapstruct.domain.PaymentViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentViMapperTest {

    private PaymentViMapper paymentViMapper;

    @BeforeEach
    void setUp() {
        paymentViMapper = new PaymentViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPaymentViSample1();
        var actual = paymentViMapper.toEntity(paymentViMapper.toDto(expected));
        assertPaymentViAllPropertiesEquals(expected, actual);
    }
}

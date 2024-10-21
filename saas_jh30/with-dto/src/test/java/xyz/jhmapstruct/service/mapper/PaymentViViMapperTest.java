package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.PaymentViViAsserts.*;
import static xyz.jhmapstruct.domain.PaymentViViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentViViMapperTest {

    private PaymentViViMapper paymentViViMapper;

    @BeforeEach
    void setUp() {
        paymentViViMapper = new PaymentViViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPaymentViViSample1();
        var actual = paymentViViMapper.toEntity(paymentViViMapper.toDto(expected));
        assertPaymentViViAllPropertiesEquals(expected, actual);
    }
}

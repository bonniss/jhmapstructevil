package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.PaymentAlphaAsserts.*;
import static xyz.jhmapstruct.domain.PaymentAlphaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentAlphaMapperTest {

    private PaymentAlphaMapper paymentAlphaMapper;

    @BeforeEach
    void setUp() {
        paymentAlphaMapper = new PaymentAlphaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPaymentAlphaSample1();
        var actual = paymentAlphaMapper.toEntity(paymentAlphaMapper.toDto(expected));
        assertPaymentAlphaAllPropertiesEquals(expected, actual);
    }
}

package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.PaymentSigmaAsserts.*;
import static xyz.jhmapstruct.domain.PaymentSigmaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentSigmaMapperTest {

    private PaymentSigmaMapper paymentSigmaMapper;

    @BeforeEach
    void setUp() {
        paymentSigmaMapper = new PaymentSigmaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPaymentSigmaSample1();
        var actual = paymentSigmaMapper.toEntity(paymentSigmaMapper.toDto(expected));
        assertPaymentSigmaAllPropertiesEquals(expected, actual);
    }
}

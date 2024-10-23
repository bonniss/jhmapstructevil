package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.PaymentBetaAsserts.*;
import static xyz.jhmapstruct.domain.PaymentBetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentBetaMapperTest {

    private PaymentBetaMapper paymentBetaMapper;

    @BeforeEach
    void setUp() {
        paymentBetaMapper = new PaymentBetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPaymentBetaSample1();
        var actual = paymentBetaMapper.toEntity(paymentBetaMapper.toDto(expected));
        assertPaymentBetaAllPropertiesEquals(expected, actual);
    }
}

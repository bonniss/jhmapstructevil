package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.PaymentGammaAsserts.*;
import static xyz.jhmapstruct.domain.PaymentGammaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentGammaMapperTest {

    private PaymentGammaMapper paymentGammaMapper;

    @BeforeEach
    void setUp() {
        paymentGammaMapper = new PaymentGammaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPaymentGammaSample1();
        var actual = paymentGammaMapper.toEntity(paymentGammaMapper.toDto(expected));
        assertPaymentGammaAllPropertiesEquals(expected, actual);
    }
}

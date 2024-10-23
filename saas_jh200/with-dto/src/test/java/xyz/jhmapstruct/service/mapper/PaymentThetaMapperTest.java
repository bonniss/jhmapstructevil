package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.PaymentThetaAsserts.*;
import static xyz.jhmapstruct.domain.PaymentThetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentThetaMapperTest {

    private PaymentThetaMapper paymentThetaMapper;

    @BeforeEach
    void setUp() {
        paymentThetaMapper = new PaymentThetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPaymentThetaSample1();
        var actual = paymentThetaMapper.toEntity(paymentThetaMapper.toDto(expected));
        assertPaymentThetaAllPropertiesEquals(expected, actual);
    }
}

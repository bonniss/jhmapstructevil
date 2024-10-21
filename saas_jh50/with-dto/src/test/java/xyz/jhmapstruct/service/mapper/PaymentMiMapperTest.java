package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.PaymentMiAsserts.*;
import static xyz.jhmapstruct.domain.PaymentMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentMiMapperTest {

    private PaymentMiMapper paymentMiMapper;

    @BeforeEach
    void setUp() {
        paymentMiMapper = new PaymentMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPaymentMiSample1();
        var actual = paymentMiMapper.toEntity(paymentMiMapper.toDto(expected));
        assertPaymentMiAllPropertiesEquals(expected, actual);
    }
}

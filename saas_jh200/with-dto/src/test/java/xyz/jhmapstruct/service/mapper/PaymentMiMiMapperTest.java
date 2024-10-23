package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.PaymentMiMiAsserts.*;
import static xyz.jhmapstruct.domain.PaymentMiMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentMiMiMapperTest {

    private PaymentMiMiMapper paymentMiMiMapper;

    @BeforeEach
    void setUp() {
        paymentMiMiMapper = new PaymentMiMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPaymentMiMiSample1();
        var actual = paymentMiMiMapper.toEntity(paymentMiMiMapper.toDto(expected));
        assertPaymentMiMiAllPropertiesEquals(expected, actual);
    }
}

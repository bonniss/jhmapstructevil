package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextPaymentSigmaAsserts.*;
import static xyz.jhmapstruct.domain.NextPaymentSigmaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextPaymentSigmaMapperTest {

    private NextPaymentSigmaMapper nextPaymentSigmaMapper;

    @BeforeEach
    void setUp() {
        nextPaymentSigmaMapper = new NextPaymentSigmaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextPaymentSigmaSample1();
        var actual = nextPaymentSigmaMapper.toEntity(nextPaymentSigmaMapper.toDto(expected));
        assertNextPaymentSigmaAllPropertiesEquals(expected, actual);
    }
}

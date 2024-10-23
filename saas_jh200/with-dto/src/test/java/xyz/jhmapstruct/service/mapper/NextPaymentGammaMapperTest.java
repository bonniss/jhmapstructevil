package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextPaymentGammaAsserts.*;
import static xyz.jhmapstruct.domain.NextPaymentGammaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextPaymentGammaMapperTest {

    private NextPaymentGammaMapper nextPaymentGammaMapper;

    @BeforeEach
    void setUp() {
        nextPaymentGammaMapper = new NextPaymentGammaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextPaymentGammaSample1();
        var actual = nextPaymentGammaMapper.toEntity(nextPaymentGammaMapper.toDto(expected));
        assertNextPaymentGammaAllPropertiesEquals(expected, actual);
    }
}

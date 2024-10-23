package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextPaymentAlphaAsserts.*;
import static xyz.jhmapstruct.domain.NextPaymentAlphaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextPaymentAlphaMapperTest {

    private NextPaymentAlphaMapper nextPaymentAlphaMapper;

    @BeforeEach
    void setUp() {
        nextPaymentAlphaMapper = new NextPaymentAlphaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextPaymentAlphaSample1();
        var actual = nextPaymentAlphaMapper.toEntity(nextPaymentAlphaMapper.toDto(expected));
        assertNextPaymentAlphaAllPropertiesEquals(expected, actual);
    }
}

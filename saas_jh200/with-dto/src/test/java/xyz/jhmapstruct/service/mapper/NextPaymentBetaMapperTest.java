package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextPaymentBetaAsserts.*;
import static xyz.jhmapstruct.domain.NextPaymentBetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextPaymentBetaMapperTest {

    private NextPaymentBetaMapper nextPaymentBetaMapper;

    @BeforeEach
    void setUp() {
        nextPaymentBetaMapper = new NextPaymentBetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextPaymentBetaSample1();
        var actual = nextPaymentBetaMapper.toEntity(nextPaymentBetaMapper.toDto(expected));
        assertNextPaymentBetaAllPropertiesEquals(expected, actual);
    }
}

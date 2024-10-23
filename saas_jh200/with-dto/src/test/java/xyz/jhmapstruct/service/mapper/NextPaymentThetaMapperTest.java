package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextPaymentThetaAsserts.*;
import static xyz.jhmapstruct.domain.NextPaymentThetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextPaymentThetaMapperTest {

    private NextPaymentThetaMapper nextPaymentThetaMapper;

    @BeforeEach
    void setUp() {
        nextPaymentThetaMapper = new NextPaymentThetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextPaymentThetaSample1();
        var actual = nextPaymentThetaMapper.toEntity(nextPaymentThetaMapper.toDto(expected));
        assertNextPaymentThetaAllPropertiesEquals(expected, actual);
    }
}

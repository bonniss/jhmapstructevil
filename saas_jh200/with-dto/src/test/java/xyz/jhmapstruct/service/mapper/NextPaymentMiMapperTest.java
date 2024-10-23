package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextPaymentMiAsserts.*;
import static xyz.jhmapstruct.domain.NextPaymentMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextPaymentMiMapperTest {

    private NextPaymentMiMapper nextPaymentMiMapper;

    @BeforeEach
    void setUp() {
        nextPaymentMiMapper = new NextPaymentMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextPaymentMiSample1();
        var actual = nextPaymentMiMapper.toEntity(nextPaymentMiMapper.toDto(expected));
        assertNextPaymentMiAllPropertiesEquals(expected, actual);
    }
}

package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextPaymentMiMiAsserts.*;
import static xyz.jhmapstruct.domain.NextPaymentMiMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextPaymentMiMiMapperTest {

    private NextPaymentMiMiMapper nextPaymentMiMiMapper;

    @BeforeEach
    void setUp() {
        nextPaymentMiMiMapper = new NextPaymentMiMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextPaymentMiMiSample1();
        var actual = nextPaymentMiMiMapper.toEntity(nextPaymentMiMiMapper.toDto(expected));
        assertNextPaymentMiMiAllPropertiesEquals(expected, actual);
    }
}

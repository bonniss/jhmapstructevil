package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextPaymentAsserts.*;
import static xyz.jhmapstruct.domain.NextPaymentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextPaymentMapperTest {

    private NextPaymentMapper nextPaymentMapper;

    @BeforeEach
    void setUp() {
        nextPaymentMapper = new NextPaymentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextPaymentSample1();
        var actual = nextPaymentMapper.toEntity(nextPaymentMapper.toDto(expected));
        assertNextPaymentAllPropertiesEquals(expected, actual);
    }
}

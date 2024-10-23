package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextPaymentViAsserts.*;
import static xyz.jhmapstruct.domain.NextPaymentViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextPaymentViMapperTest {

    private NextPaymentViMapper nextPaymentViMapper;

    @BeforeEach
    void setUp() {
        nextPaymentViMapper = new NextPaymentViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextPaymentViSample1();
        var actual = nextPaymentViMapper.toEntity(nextPaymentViMapper.toDto(expected));
        assertNextPaymentViAllPropertiesEquals(expected, actual);
    }
}

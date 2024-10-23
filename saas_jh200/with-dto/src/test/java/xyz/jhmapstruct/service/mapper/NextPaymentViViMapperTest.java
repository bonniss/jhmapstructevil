package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextPaymentViViAsserts.*;
import static xyz.jhmapstruct.domain.NextPaymentViViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextPaymentViViMapperTest {

    private NextPaymentViViMapper nextPaymentViViMapper;

    @BeforeEach
    void setUp() {
        nextPaymentViViMapper = new NextPaymentViViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextPaymentViViSample1();
        var actual = nextPaymentViViMapper.toEntity(nextPaymentViViMapper.toDto(expected));
        assertNextPaymentViViAllPropertiesEquals(expected, actual);
    }
}

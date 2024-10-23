package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextInvoiceViViAsserts.*;
import static xyz.jhmapstruct.domain.NextInvoiceViViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextInvoiceViViMapperTest {

    private NextInvoiceViViMapper nextInvoiceViViMapper;

    @BeforeEach
    void setUp() {
        nextInvoiceViViMapper = new NextInvoiceViViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextInvoiceViViSample1();
        var actual = nextInvoiceViViMapper.toEntity(nextInvoiceViViMapper.toDto(expected));
        assertNextInvoiceViViAllPropertiesEquals(expected, actual);
    }
}

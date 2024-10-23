package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextInvoiceViAsserts.*;
import static xyz.jhmapstruct.domain.NextInvoiceViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextInvoiceViMapperTest {

    private NextInvoiceViMapper nextInvoiceViMapper;

    @BeforeEach
    void setUp() {
        nextInvoiceViMapper = new NextInvoiceViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextInvoiceViSample1();
        var actual = nextInvoiceViMapper.toEntity(nextInvoiceViMapper.toDto(expected));
        assertNextInvoiceViAllPropertiesEquals(expected, actual);
    }
}

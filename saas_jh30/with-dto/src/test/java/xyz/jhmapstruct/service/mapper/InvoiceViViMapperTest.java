package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.InvoiceViViAsserts.*;
import static xyz.jhmapstruct.domain.InvoiceViViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoiceViViMapperTest {

    private InvoiceViViMapper invoiceViViMapper;

    @BeforeEach
    void setUp() {
        invoiceViViMapper = new InvoiceViViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInvoiceViViSample1();
        var actual = invoiceViViMapper.toEntity(invoiceViViMapper.toDto(expected));
        assertInvoiceViViAllPropertiesEquals(expected, actual);
    }
}

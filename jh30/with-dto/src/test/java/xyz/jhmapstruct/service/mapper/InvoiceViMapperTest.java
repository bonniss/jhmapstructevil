package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.InvoiceViAsserts.*;
import static xyz.jhmapstruct.domain.InvoiceViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoiceViMapperTest {

    private InvoiceViMapper invoiceViMapper;

    @BeforeEach
    void setUp() {
        invoiceViMapper = new InvoiceViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInvoiceViSample1();
        var actual = invoiceViMapper.toEntity(invoiceViMapper.toDto(expected));
        assertInvoiceViAllPropertiesEquals(expected, actual);
    }
}

package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.InvoiceAsserts.*;
import static xyz.jhmapstruct.domain.InvoiceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoiceMapperTest {

    private InvoiceMapper invoiceMapper;

    @BeforeEach
    void setUp() {
        invoiceMapper = new InvoiceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInvoiceSample1();
        var actual = invoiceMapper.toEntity(invoiceMapper.toDto(expected));
        assertInvoiceAllPropertiesEquals(expected, actual);
    }
}

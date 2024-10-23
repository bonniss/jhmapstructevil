package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.InvoiceAlphaAsserts.*;
import static xyz.jhmapstruct.domain.InvoiceAlphaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoiceAlphaMapperTest {

    private InvoiceAlphaMapper invoiceAlphaMapper;

    @BeforeEach
    void setUp() {
        invoiceAlphaMapper = new InvoiceAlphaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInvoiceAlphaSample1();
        var actual = invoiceAlphaMapper.toEntity(invoiceAlphaMapper.toDto(expected));
        assertInvoiceAlphaAllPropertiesEquals(expected, actual);
    }
}

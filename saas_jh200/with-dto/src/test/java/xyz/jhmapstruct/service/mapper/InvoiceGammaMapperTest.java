package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.InvoiceGammaAsserts.*;
import static xyz.jhmapstruct.domain.InvoiceGammaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoiceGammaMapperTest {

    private InvoiceGammaMapper invoiceGammaMapper;

    @BeforeEach
    void setUp() {
        invoiceGammaMapper = new InvoiceGammaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInvoiceGammaSample1();
        var actual = invoiceGammaMapper.toEntity(invoiceGammaMapper.toDto(expected));
        assertInvoiceGammaAllPropertiesEquals(expected, actual);
    }
}

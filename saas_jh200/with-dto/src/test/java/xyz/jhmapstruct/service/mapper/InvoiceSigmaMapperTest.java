package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.InvoiceSigmaAsserts.*;
import static xyz.jhmapstruct.domain.InvoiceSigmaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoiceSigmaMapperTest {

    private InvoiceSigmaMapper invoiceSigmaMapper;

    @BeforeEach
    void setUp() {
        invoiceSigmaMapper = new InvoiceSigmaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInvoiceSigmaSample1();
        var actual = invoiceSigmaMapper.toEntity(invoiceSigmaMapper.toDto(expected));
        assertInvoiceSigmaAllPropertiesEquals(expected, actual);
    }
}

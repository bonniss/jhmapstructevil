package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.InvoiceBetaAsserts.*;
import static xyz.jhmapstruct.domain.InvoiceBetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoiceBetaMapperTest {

    private InvoiceBetaMapper invoiceBetaMapper;

    @BeforeEach
    void setUp() {
        invoiceBetaMapper = new InvoiceBetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInvoiceBetaSample1();
        var actual = invoiceBetaMapper.toEntity(invoiceBetaMapper.toDto(expected));
        assertInvoiceBetaAllPropertiesEquals(expected, actual);
    }
}

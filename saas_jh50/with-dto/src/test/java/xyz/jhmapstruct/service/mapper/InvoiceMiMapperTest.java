package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.InvoiceMiAsserts.*;
import static xyz.jhmapstruct.domain.InvoiceMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoiceMiMapperTest {

    private InvoiceMiMapper invoiceMiMapper;

    @BeforeEach
    void setUp() {
        invoiceMiMapper = new InvoiceMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInvoiceMiSample1();
        var actual = invoiceMiMapper.toEntity(invoiceMiMapper.toDto(expected));
        assertInvoiceMiAllPropertiesEquals(expected, actual);
    }
}

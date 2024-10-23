package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.InvoiceMiMiAsserts.*;
import static xyz.jhmapstruct.domain.InvoiceMiMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoiceMiMiMapperTest {

    private InvoiceMiMiMapper invoiceMiMiMapper;

    @BeforeEach
    void setUp() {
        invoiceMiMiMapper = new InvoiceMiMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInvoiceMiMiSample1();
        var actual = invoiceMiMiMapper.toEntity(invoiceMiMiMapper.toDto(expected));
        assertInvoiceMiMiAllPropertiesEquals(expected, actual);
    }
}

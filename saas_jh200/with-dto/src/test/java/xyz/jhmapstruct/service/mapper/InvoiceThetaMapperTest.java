package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.InvoiceThetaAsserts.*;
import static xyz.jhmapstruct.domain.InvoiceThetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoiceThetaMapperTest {

    private InvoiceThetaMapper invoiceThetaMapper;

    @BeforeEach
    void setUp() {
        invoiceThetaMapper = new InvoiceThetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInvoiceThetaSample1();
        var actual = invoiceThetaMapper.toEntity(invoiceThetaMapper.toDto(expected));
        assertInvoiceThetaAllPropertiesEquals(expected, actual);
    }
}

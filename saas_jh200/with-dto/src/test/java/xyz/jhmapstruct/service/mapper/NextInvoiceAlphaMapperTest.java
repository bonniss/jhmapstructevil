package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextInvoiceAlphaAsserts.*;
import static xyz.jhmapstruct.domain.NextInvoiceAlphaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextInvoiceAlphaMapperTest {

    private NextInvoiceAlphaMapper nextInvoiceAlphaMapper;

    @BeforeEach
    void setUp() {
        nextInvoiceAlphaMapper = new NextInvoiceAlphaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextInvoiceAlphaSample1();
        var actual = nextInvoiceAlphaMapper.toEntity(nextInvoiceAlphaMapper.toDto(expected));
        assertNextInvoiceAlphaAllPropertiesEquals(expected, actual);
    }
}

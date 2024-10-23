package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextInvoiceAsserts.*;
import static xyz.jhmapstruct.domain.NextInvoiceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextInvoiceMapperTest {

    private NextInvoiceMapper nextInvoiceMapper;

    @BeforeEach
    void setUp() {
        nextInvoiceMapper = new NextInvoiceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextInvoiceSample1();
        var actual = nextInvoiceMapper.toEntity(nextInvoiceMapper.toDto(expected));
        assertNextInvoiceAllPropertiesEquals(expected, actual);
    }
}

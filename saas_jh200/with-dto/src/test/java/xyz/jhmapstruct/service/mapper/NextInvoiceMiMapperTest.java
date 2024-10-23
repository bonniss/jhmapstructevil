package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextInvoiceMiAsserts.*;
import static xyz.jhmapstruct.domain.NextInvoiceMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextInvoiceMiMapperTest {

    private NextInvoiceMiMapper nextInvoiceMiMapper;

    @BeforeEach
    void setUp() {
        nextInvoiceMiMapper = new NextInvoiceMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextInvoiceMiSample1();
        var actual = nextInvoiceMiMapper.toEntity(nextInvoiceMiMapper.toDto(expected));
        assertNextInvoiceMiAllPropertiesEquals(expected, actual);
    }
}

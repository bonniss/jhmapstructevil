package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextInvoiceMiMiAsserts.*;
import static xyz.jhmapstruct.domain.NextInvoiceMiMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextInvoiceMiMiMapperTest {

    private NextInvoiceMiMiMapper nextInvoiceMiMiMapper;

    @BeforeEach
    void setUp() {
        nextInvoiceMiMiMapper = new NextInvoiceMiMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextInvoiceMiMiSample1();
        var actual = nextInvoiceMiMiMapper.toEntity(nextInvoiceMiMiMapper.toDto(expected));
        assertNextInvoiceMiMiAllPropertiesEquals(expected, actual);
    }
}

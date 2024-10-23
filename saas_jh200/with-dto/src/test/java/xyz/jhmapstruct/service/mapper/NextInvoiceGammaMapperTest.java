package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextInvoiceGammaAsserts.*;
import static xyz.jhmapstruct.domain.NextInvoiceGammaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextInvoiceGammaMapperTest {

    private NextInvoiceGammaMapper nextInvoiceGammaMapper;

    @BeforeEach
    void setUp() {
        nextInvoiceGammaMapper = new NextInvoiceGammaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextInvoiceGammaSample1();
        var actual = nextInvoiceGammaMapper.toEntity(nextInvoiceGammaMapper.toDto(expected));
        assertNextInvoiceGammaAllPropertiesEquals(expected, actual);
    }
}

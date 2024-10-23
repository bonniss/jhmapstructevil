package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextInvoiceSigmaAsserts.*;
import static xyz.jhmapstruct.domain.NextInvoiceSigmaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextInvoiceSigmaMapperTest {

    private NextInvoiceSigmaMapper nextInvoiceSigmaMapper;

    @BeforeEach
    void setUp() {
        nextInvoiceSigmaMapper = new NextInvoiceSigmaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextInvoiceSigmaSample1();
        var actual = nextInvoiceSigmaMapper.toEntity(nextInvoiceSigmaMapper.toDto(expected));
        assertNextInvoiceSigmaAllPropertiesEquals(expected, actual);
    }
}

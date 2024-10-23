package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextInvoiceBetaAsserts.*;
import static xyz.jhmapstruct.domain.NextInvoiceBetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextInvoiceBetaMapperTest {

    private NextInvoiceBetaMapper nextInvoiceBetaMapper;

    @BeforeEach
    void setUp() {
        nextInvoiceBetaMapper = new NextInvoiceBetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextInvoiceBetaSample1();
        var actual = nextInvoiceBetaMapper.toEntity(nextInvoiceBetaMapper.toDto(expected));
        assertNextInvoiceBetaAllPropertiesEquals(expected, actual);
    }
}

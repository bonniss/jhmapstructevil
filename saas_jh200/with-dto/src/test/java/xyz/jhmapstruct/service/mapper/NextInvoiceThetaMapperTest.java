package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextInvoiceThetaAsserts.*;
import static xyz.jhmapstruct.domain.NextInvoiceThetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextInvoiceThetaMapperTest {

    private NextInvoiceThetaMapper nextInvoiceThetaMapper;

    @BeforeEach
    void setUp() {
        nextInvoiceThetaMapper = new NextInvoiceThetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextInvoiceThetaSample1();
        var actual = nextInvoiceThetaMapper.toEntity(nextInvoiceThetaMapper.toDto(expected));
        assertNextInvoiceThetaAllPropertiesEquals(expected, actual);
    }
}

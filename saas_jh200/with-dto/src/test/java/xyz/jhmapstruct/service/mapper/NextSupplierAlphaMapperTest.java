package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextSupplierAlphaAsserts.*;
import static xyz.jhmapstruct.domain.NextSupplierAlphaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextSupplierAlphaMapperTest {

    private NextSupplierAlphaMapper nextSupplierAlphaMapper;

    @BeforeEach
    void setUp() {
        nextSupplierAlphaMapper = new NextSupplierAlphaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextSupplierAlphaSample1();
        var actual = nextSupplierAlphaMapper.toEntity(nextSupplierAlphaMapper.toDto(expected));
        assertNextSupplierAlphaAllPropertiesEquals(expected, actual);
    }
}

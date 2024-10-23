package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextSupplierGammaAsserts.*;
import static xyz.jhmapstruct.domain.NextSupplierGammaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextSupplierGammaMapperTest {

    private NextSupplierGammaMapper nextSupplierGammaMapper;

    @BeforeEach
    void setUp() {
        nextSupplierGammaMapper = new NextSupplierGammaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextSupplierGammaSample1();
        var actual = nextSupplierGammaMapper.toEntity(nextSupplierGammaMapper.toDto(expected));
        assertNextSupplierGammaAllPropertiesEquals(expected, actual);
    }
}

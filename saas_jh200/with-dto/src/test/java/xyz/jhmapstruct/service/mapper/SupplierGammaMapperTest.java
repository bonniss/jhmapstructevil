package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.SupplierGammaAsserts.*;
import static xyz.jhmapstruct.domain.SupplierGammaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SupplierGammaMapperTest {

    private SupplierGammaMapper supplierGammaMapper;

    @BeforeEach
    void setUp() {
        supplierGammaMapper = new SupplierGammaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSupplierGammaSample1();
        var actual = supplierGammaMapper.toEntity(supplierGammaMapper.toDto(expected));
        assertSupplierGammaAllPropertiesEquals(expected, actual);
    }
}

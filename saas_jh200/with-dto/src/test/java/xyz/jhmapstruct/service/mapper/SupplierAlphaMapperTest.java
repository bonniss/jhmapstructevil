package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.SupplierAlphaAsserts.*;
import static xyz.jhmapstruct.domain.SupplierAlphaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SupplierAlphaMapperTest {

    private SupplierAlphaMapper supplierAlphaMapper;

    @BeforeEach
    void setUp() {
        supplierAlphaMapper = new SupplierAlphaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSupplierAlphaSample1();
        var actual = supplierAlphaMapper.toEntity(supplierAlphaMapper.toDto(expected));
        assertSupplierAlphaAllPropertiesEquals(expected, actual);
    }
}

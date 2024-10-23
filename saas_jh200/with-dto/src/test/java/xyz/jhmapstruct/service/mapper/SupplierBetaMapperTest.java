package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.SupplierBetaAsserts.*;
import static xyz.jhmapstruct.domain.SupplierBetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SupplierBetaMapperTest {

    private SupplierBetaMapper supplierBetaMapper;

    @BeforeEach
    void setUp() {
        supplierBetaMapper = new SupplierBetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSupplierBetaSample1();
        var actual = supplierBetaMapper.toEntity(supplierBetaMapper.toDto(expected));
        assertSupplierBetaAllPropertiesEquals(expected, actual);
    }
}

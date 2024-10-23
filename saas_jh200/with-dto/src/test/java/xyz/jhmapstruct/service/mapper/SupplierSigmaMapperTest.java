package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.SupplierSigmaAsserts.*;
import static xyz.jhmapstruct.domain.SupplierSigmaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SupplierSigmaMapperTest {

    private SupplierSigmaMapper supplierSigmaMapper;

    @BeforeEach
    void setUp() {
        supplierSigmaMapper = new SupplierSigmaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSupplierSigmaSample1();
        var actual = supplierSigmaMapper.toEntity(supplierSigmaMapper.toDto(expected));
        assertSupplierSigmaAllPropertiesEquals(expected, actual);
    }
}

package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.SupplierMiAsserts.*;
import static xyz.jhmapstruct.domain.SupplierMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SupplierMiMapperTest {

    private SupplierMiMapper supplierMiMapper;

    @BeforeEach
    void setUp() {
        supplierMiMapper = new SupplierMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSupplierMiSample1();
        var actual = supplierMiMapper.toEntity(supplierMiMapper.toDto(expected));
        assertSupplierMiAllPropertiesEquals(expected, actual);
    }
}

package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.SupplierMiMiAsserts.*;
import static xyz.jhmapstruct.domain.SupplierMiMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SupplierMiMiMapperTest {

    private SupplierMiMiMapper supplierMiMiMapper;

    @BeforeEach
    void setUp() {
        supplierMiMiMapper = new SupplierMiMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSupplierMiMiSample1();
        var actual = supplierMiMiMapper.toEntity(supplierMiMiMapper.toDto(expected));
        assertSupplierMiMiAllPropertiesEquals(expected, actual);
    }
}

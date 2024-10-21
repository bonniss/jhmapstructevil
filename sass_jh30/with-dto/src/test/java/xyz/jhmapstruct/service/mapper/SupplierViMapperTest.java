package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.SupplierViAsserts.*;
import static xyz.jhmapstruct.domain.SupplierViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SupplierViMapperTest {

    private SupplierViMapper supplierViMapper;

    @BeforeEach
    void setUp() {
        supplierViMapper = new SupplierViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSupplierViSample1();
        var actual = supplierViMapper.toEntity(supplierViMapper.toDto(expected));
        assertSupplierViAllPropertiesEquals(expected, actual);
    }
}

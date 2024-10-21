package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.SupplierViViAsserts.*;
import static xyz.jhmapstruct.domain.SupplierViViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SupplierViViMapperTest {

    private SupplierViViMapper supplierViViMapper;

    @BeforeEach
    void setUp() {
        supplierViViMapper = new SupplierViViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSupplierViViSample1();
        var actual = supplierViViMapper.toEntity(supplierViViMapper.toDto(expected));
        assertSupplierViViAllPropertiesEquals(expected, actual);
    }
}

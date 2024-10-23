package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.SupplierThetaAsserts.*;
import static xyz.jhmapstruct.domain.SupplierThetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SupplierThetaMapperTest {

    private SupplierThetaMapper supplierThetaMapper;

    @BeforeEach
    void setUp() {
        supplierThetaMapper = new SupplierThetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSupplierThetaSample1();
        var actual = supplierThetaMapper.toEntity(supplierThetaMapper.toDto(expected));
        assertSupplierThetaAllPropertiesEquals(expected, actual);
    }
}

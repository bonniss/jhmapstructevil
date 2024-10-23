package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextSupplierThetaAsserts.*;
import static xyz.jhmapstruct.domain.NextSupplierThetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextSupplierThetaMapperTest {

    private NextSupplierThetaMapper nextSupplierThetaMapper;

    @BeforeEach
    void setUp() {
        nextSupplierThetaMapper = new NextSupplierThetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextSupplierThetaSample1();
        var actual = nextSupplierThetaMapper.toEntity(nextSupplierThetaMapper.toDto(expected));
        assertNextSupplierThetaAllPropertiesEquals(expected, actual);
    }
}

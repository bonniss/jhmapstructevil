package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextSupplierSigmaAsserts.*;
import static xyz.jhmapstruct.domain.NextSupplierSigmaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextSupplierSigmaMapperTest {

    private NextSupplierSigmaMapper nextSupplierSigmaMapper;

    @BeforeEach
    void setUp() {
        nextSupplierSigmaMapper = new NextSupplierSigmaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextSupplierSigmaSample1();
        var actual = nextSupplierSigmaMapper.toEntity(nextSupplierSigmaMapper.toDto(expected));
        assertNextSupplierSigmaAllPropertiesEquals(expected, actual);
    }
}

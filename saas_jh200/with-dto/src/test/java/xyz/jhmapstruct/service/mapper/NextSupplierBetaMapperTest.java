package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextSupplierBetaAsserts.*;
import static xyz.jhmapstruct.domain.NextSupplierBetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextSupplierBetaMapperTest {

    private NextSupplierBetaMapper nextSupplierBetaMapper;

    @BeforeEach
    void setUp() {
        nextSupplierBetaMapper = new NextSupplierBetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextSupplierBetaSample1();
        var actual = nextSupplierBetaMapper.toEntity(nextSupplierBetaMapper.toDto(expected));
        assertNextSupplierBetaAllPropertiesEquals(expected, actual);
    }
}

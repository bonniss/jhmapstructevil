package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ProductAlphaAsserts.*;
import static xyz.jhmapstruct.domain.ProductAlphaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductAlphaMapperTest {

    private ProductAlphaMapper productAlphaMapper;

    @BeforeEach
    void setUp() {
        productAlphaMapper = new ProductAlphaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductAlphaSample1();
        var actual = productAlphaMapper.toEntity(productAlphaMapper.toDto(expected));
        assertProductAlphaAllPropertiesEquals(expected, actual);
    }
}

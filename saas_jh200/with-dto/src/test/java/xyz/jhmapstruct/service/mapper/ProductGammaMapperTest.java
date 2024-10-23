package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ProductGammaAsserts.*;
import static xyz.jhmapstruct.domain.ProductGammaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductGammaMapperTest {

    private ProductGammaMapper productGammaMapper;

    @BeforeEach
    void setUp() {
        productGammaMapper = new ProductGammaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductGammaSample1();
        var actual = productGammaMapper.toEntity(productGammaMapper.toDto(expected));
        assertProductGammaAllPropertiesEquals(expected, actual);
    }
}

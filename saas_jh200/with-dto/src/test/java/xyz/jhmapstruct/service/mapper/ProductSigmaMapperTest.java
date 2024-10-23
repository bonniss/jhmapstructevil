package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ProductSigmaAsserts.*;
import static xyz.jhmapstruct.domain.ProductSigmaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductSigmaMapperTest {

    private ProductSigmaMapper productSigmaMapper;

    @BeforeEach
    void setUp() {
        productSigmaMapper = new ProductSigmaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductSigmaSample1();
        var actual = productSigmaMapper.toEntity(productSigmaMapper.toDto(expected));
        assertProductSigmaAllPropertiesEquals(expected, actual);
    }
}

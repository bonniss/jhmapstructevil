package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ProductMiMiAsserts.*;
import static xyz.jhmapstruct.domain.ProductMiMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductMiMiMapperTest {

    private ProductMiMiMapper productMiMiMapper;

    @BeforeEach
    void setUp() {
        productMiMiMapper = new ProductMiMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductMiMiSample1();
        var actual = productMiMiMapper.toEntity(productMiMiMapper.toDto(expected));
        assertProductMiMiAllPropertiesEquals(expected, actual);
    }
}

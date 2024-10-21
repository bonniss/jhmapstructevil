package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ProductMiAsserts.*;
import static xyz.jhmapstruct.domain.ProductMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductMiMapperTest {

    private ProductMiMapper productMiMapper;

    @BeforeEach
    void setUp() {
        productMiMapper = new ProductMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductMiSample1();
        var actual = productMiMapper.toEntity(productMiMapper.toDto(expected));
        assertProductMiAllPropertiesEquals(expected, actual);
    }
}

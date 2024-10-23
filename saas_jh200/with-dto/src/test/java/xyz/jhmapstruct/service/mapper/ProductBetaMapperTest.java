package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ProductBetaAsserts.*;
import static xyz.jhmapstruct.domain.ProductBetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductBetaMapperTest {

    private ProductBetaMapper productBetaMapper;

    @BeforeEach
    void setUp() {
        productBetaMapper = new ProductBetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductBetaSample1();
        var actual = productBetaMapper.toEntity(productBetaMapper.toDto(expected));
        assertProductBetaAllPropertiesEquals(expected, actual);
    }
}

package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ProductThetaAsserts.*;
import static xyz.jhmapstruct.domain.ProductThetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductThetaMapperTest {

    private ProductThetaMapper productThetaMapper;

    @BeforeEach
    void setUp() {
        productThetaMapper = new ProductThetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductThetaSample1();
        var actual = productThetaMapper.toEntity(productThetaMapper.toDto(expected));
        assertProductThetaAllPropertiesEquals(expected, actual);
    }
}

package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ProductViAsserts.*;
import static xyz.jhmapstruct.domain.ProductViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductViMapperTest {

    private ProductViMapper productViMapper;

    @BeforeEach
    void setUp() {
        productViMapper = new ProductViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductViSample1();
        var actual = productViMapper.toEntity(productViMapper.toDto(expected));
        assertProductViAllPropertiesEquals(expected, actual);
    }
}

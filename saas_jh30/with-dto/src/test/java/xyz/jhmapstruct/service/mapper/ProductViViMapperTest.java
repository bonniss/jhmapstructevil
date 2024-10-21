package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.ProductViViAsserts.*;
import static xyz.jhmapstruct.domain.ProductViViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductViViMapperTest {

    private ProductViViMapper productViViMapper;

    @BeforeEach
    void setUp() {
        productViViMapper = new ProductViViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductViViSample1();
        var actual = productViViMapper.toEntity(productViViMapper.toDto(expected));
        assertProductViViAllPropertiesEquals(expected, actual);
    }
}

package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.CategorySigmaAsserts.*;
import static xyz.jhmapstruct.domain.CategorySigmaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategorySigmaMapperTest {

    private CategorySigmaMapper categorySigmaMapper;

    @BeforeEach
    void setUp() {
        categorySigmaMapper = new CategorySigmaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCategorySigmaSample1();
        var actual = categorySigmaMapper.toEntity(categorySigmaMapper.toDto(expected));
        assertCategorySigmaAllPropertiesEquals(expected, actual);
    }
}

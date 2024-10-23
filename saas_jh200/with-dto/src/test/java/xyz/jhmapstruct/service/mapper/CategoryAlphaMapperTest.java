package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.CategoryAlphaAsserts.*;
import static xyz.jhmapstruct.domain.CategoryAlphaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryAlphaMapperTest {

    private CategoryAlphaMapper categoryAlphaMapper;

    @BeforeEach
    void setUp() {
        categoryAlphaMapper = new CategoryAlphaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCategoryAlphaSample1();
        var actual = categoryAlphaMapper.toEntity(categoryAlphaMapper.toDto(expected));
        assertCategoryAlphaAllPropertiesEquals(expected, actual);
    }
}

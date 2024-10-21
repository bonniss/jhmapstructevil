package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.CategoryMiMiAsserts.*;
import static xyz.jhmapstruct.domain.CategoryMiMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryMiMiMapperTest {

    private CategoryMiMiMapper categoryMiMiMapper;

    @BeforeEach
    void setUp() {
        categoryMiMiMapper = new CategoryMiMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCategoryMiMiSample1();
        var actual = categoryMiMiMapper.toEntity(categoryMiMiMapper.toDto(expected));
        assertCategoryMiMiAllPropertiesEquals(expected, actual);
    }
}

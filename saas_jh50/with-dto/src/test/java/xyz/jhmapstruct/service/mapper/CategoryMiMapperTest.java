package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.CategoryMiAsserts.*;
import static xyz.jhmapstruct.domain.CategoryMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryMiMapperTest {

    private CategoryMiMapper categoryMiMapper;

    @BeforeEach
    void setUp() {
        categoryMiMapper = new CategoryMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCategoryMiSample1();
        var actual = categoryMiMapper.toEntity(categoryMiMapper.toDto(expected));
        assertCategoryMiAllPropertiesEquals(expected, actual);
    }
}

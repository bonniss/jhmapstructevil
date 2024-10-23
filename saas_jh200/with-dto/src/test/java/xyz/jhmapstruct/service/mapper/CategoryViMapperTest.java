package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.CategoryViAsserts.*;
import static xyz.jhmapstruct.domain.CategoryViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryViMapperTest {

    private CategoryViMapper categoryViMapper;

    @BeforeEach
    void setUp() {
        categoryViMapper = new CategoryViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCategoryViSample1();
        var actual = categoryViMapper.toEntity(categoryViMapper.toDto(expected));
        assertCategoryViAllPropertiesEquals(expected, actual);
    }
}

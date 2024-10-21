package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.CategoryViViAsserts.*;
import static xyz.jhmapstruct.domain.CategoryViViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryViViMapperTest {

    private CategoryViViMapper categoryViViMapper;

    @BeforeEach
    void setUp() {
        categoryViViMapper = new CategoryViViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCategoryViViSample1();
        var actual = categoryViViMapper.toEntity(categoryViViMapper.toDto(expected));
        assertCategoryViViAllPropertiesEquals(expected, actual);
    }
}

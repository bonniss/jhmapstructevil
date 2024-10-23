package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.CategoryBetaAsserts.*;
import static xyz.jhmapstruct.domain.CategoryBetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryBetaMapperTest {

    private CategoryBetaMapper categoryBetaMapper;

    @BeforeEach
    void setUp() {
        categoryBetaMapper = new CategoryBetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCategoryBetaSample1();
        var actual = categoryBetaMapper.toEntity(categoryBetaMapper.toDto(expected));
        assertCategoryBetaAllPropertiesEquals(expected, actual);
    }
}

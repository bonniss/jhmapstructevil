package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.CategoryGammaAsserts.*;
import static xyz.jhmapstruct.domain.CategoryGammaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryGammaMapperTest {

    private CategoryGammaMapper categoryGammaMapper;

    @BeforeEach
    void setUp() {
        categoryGammaMapper = new CategoryGammaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCategoryGammaSample1();
        var actual = categoryGammaMapper.toEntity(categoryGammaMapper.toDto(expected));
        assertCategoryGammaAllPropertiesEquals(expected, actual);
    }
}

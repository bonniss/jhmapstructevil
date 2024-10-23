package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.CategoryThetaAsserts.*;
import static xyz.jhmapstruct.domain.CategoryThetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryThetaMapperTest {

    private CategoryThetaMapper categoryThetaMapper;

    @BeforeEach
    void setUp() {
        categoryThetaMapper = new CategoryThetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCategoryThetaSample1();
        var actual = categoryThetaMapper.toEntity(categoryThetaMapper.toDto(expected));
        assertCategoryThetaAllPropertiesEquals(expected, actual);
    }
}

package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextCategoryGammaAsserts.*;
import static xyz.jhmapstruct.domain.NextCategoryGammaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextCategoryGammaMapperTest {

    private NextCategoryGammaMapper nextCategoryGammaMapper;

    @BeforeEach
    void setUp() {
        nextCategoryGammaMapper = new NextCategoryGammaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextCategoryGammaSample1();
        var actual = nextCategoryGammaMapper.toEntity(nextCategoryGammaMapper.toDto(expected));
        assertNextCategoryGammaAllPropertiesEquals(expected, actual);
    }
}

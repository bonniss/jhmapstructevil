package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextCategoryAlphaAsserts.*;
import static xyz.jhmapstruct.domain.NextCategoryAlphaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextCategoryAlphaMapperTest {

    private NextCategoryAlphaMapper nextCategoryAlphaMapper;

    @BeforeEach
    void setUp() {
        nextCategoryAlphaMapper = new NextCategoryAlphaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextCategoryAlphaSample1();
        var actual = nextCategoryAlphaMapper.toEntity(nextCategoryAlphaMapper.toDto(expected));
        assertNextCategoryAlphaAllPropertiesEquals(expected, actual);
    }
}

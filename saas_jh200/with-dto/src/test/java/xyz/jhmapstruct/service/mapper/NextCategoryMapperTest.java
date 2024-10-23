package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextCategoryAsserts.*;
import static xyz.jhmapstruct.domain.NextCategoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextCategoryMapperTest {

    private NextCategoryMapper nextCategoryMapper;

    @BeforeEach
    void setUp() {
        nextCategoryMapper = new NextCategoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextCategorySample1();
        var actual = nextCategoryMapper.toEntity(nextCategoryMapper.toDto(expected));
        assertNextCategoryAllPropertiesEquals(expected, actual);
    }
}

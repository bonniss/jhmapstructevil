package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextCategoryMiMiAsserts.*;
import static xyz.jhmapstruct.domain.NextCategoryMiMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextCategoryMiMiMapperTest {

    private NextCategoryMiMiMapper nextCategoryMiMiMapper;

    @BeforeEach
    void setUp() {
        nextCategoryMiMiMapper = new NextCategoryMiMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextCategoryMiMiSample1();
        var actual = nextCategoryMiMiMapper.toEntity(nextCategoryMiMiMapper.toDto(expected));
        assertNextCategoryMiMiAllPropertiesEquals(expected, actual);
    }
}

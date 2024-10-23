package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextCategoryMiAsserts.*;
import static xyz.jhmapstruct.domain.NextCategoryMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextCategoryMiMapperTest {

    private NextCategoryMiMapper nextCategoryMiMapper;

    @BeforeEach
    void setUp() {
        nextCategoryMiMapper = new NextCategoryMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextCategoryMiSample1();
        var actual = nextCategoryMiMapper.toEntity(nextCategoryMiMapper.toDto(expected));
        assertNextCategoryMiAllPropertiesEquals(expected, actual);
    }
}

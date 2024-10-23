package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextCategoryViViAsserts.*;
import static xyz.jhmapstruct.domain.NextCategoryViViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextCategoryViViMapperTest {

    private NextCategoryViViMapper nextCategoryViViMapper;

    @BeforeEach
    void setUp() {
        nextCategoryViViMapper = new NextCategoryViViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextCategoryViViSample1();
        var actual = nextCategoryViViMapper.toEntity(nextCategoryViViMapper.toDto(expected));
        assertNextCategoryViViAllPropertiesEquals(expected, actual);
    }
}

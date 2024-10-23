package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextCategoryViAsserts.*;
import static xyz.jhmapstruct.domain.NextCategoryViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextCategoryViMapperTest {

    private NextCategoryViMapper nextCategoryViMapper;

    @BeforeEach
    void setUp() {
        nextCategoryViMapper = new NextCategoryViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextCategoryViSample1();
        var actual = nextCategoryViMapper.toEntity(nextCategoryViMapper.toDto(expected));
        assertNextCategoryViAllPropertiesEquals(expected, actual);
    }
}

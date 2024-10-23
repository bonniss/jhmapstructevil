package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextCategorySigmaAsserts.*;
import static xyz.jhmapstruct.domain.NextCategorySigmaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextCategorySigmaMapperTest {

    private NextCategorySigmaMapper nextCategorySigmaMapper;

    @BeforeEach
    void setUp() {
        nextCategorySigmaMapper = new NextCategorySigmaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextCategorySigmaSample1();
        var actual = nextCategorySigmaMapper.toEntity(nextCategorySigmaMapper.toDto(expected));
        assertNextCategorySigmaAllPropertiesEquals(expected, actual);
    }
}

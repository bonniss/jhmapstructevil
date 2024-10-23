package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextCategoryBetaAsserts.*;
import static xyz.jhmapstruct.domain.NextCategoryBetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextCategoryBetaMapperTest {

    private NextCategoryBetaMapper nextCategoryBetaMapper;

    @BeforeEach
    void setUp() {
        nextCategoryBetaMapper = new NextCategoryBetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextCategoryBetaSample1();
        var actual = nextCategoryBetaMapper.toEntity(nextCategoryBetaMapper.toDto(expected));
        assertNextCategoryBetaAllPropertiesEquals(expected, actual);
    }
}

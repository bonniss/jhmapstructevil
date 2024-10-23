package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextCategoryThetaAsserts.*;
import static xyz.jhmapstruct.domain.NextCategoryThetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextCategoryThetaMapperTest {

    private NextCategoryThetaMapper nextCategoryThetaMapper;

    @BeforeEach
    void setUp() {
        nextCategoryThetaMapper = new NextCategoryThetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextCategoryThetaSample1();
        var actual = nextCategoryThetaMapper.toEntity(nextCategoryThetaMapper.toDto(expected));
        assertNextCategoryThetaAllPropertiesEquals(expected, actual);
    }
}

package ai.realworld.service.mapper;

import static ai.realworld.domain.AlBetonamuRelationAsserts.*;
import static ai.realworld.domain.AlBetonamuRelationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlBetonamuRelationMapperTest {

    private AlBetonamuRelationMapper alBetonamuRelationMapper;

    @BeforeEach
    void setUp() {
        alBetonamuRelationMapper = new AlBetonamuRelationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlBetonamuRelationSample1();
        var actual = alBetonamuRelationMapper.toEntity(alBetonamuRelationMapper.toDto(expected));
        assertAlBetonamuRelationAllPropertiesEquals(expected, actual);
    }
}

package ai.realworld.service.mapper;

import static ai.realworld.domain.AlBetonamuRelationViAsserts.*;
import static ai.realworld.domain.AlBetonamuRelationViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlBetonamuRelationViMapperTest {

    private AlBetonamuRelationViMapper alBetonamuRelationViMapper;

    @BeforeEach
    void setUp() {
        alBetonamuRelationViMapper = new AlBetonamuRelationViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlBetonamuRelationViSample1();
        var actual = alBetonamuRelationViMapper.toEntity(alBetonamuRelationViMapper.toDto(expected));
        assertAlBetonamuRelationViAllPropertiesEquals(expected, actual);
    }
}

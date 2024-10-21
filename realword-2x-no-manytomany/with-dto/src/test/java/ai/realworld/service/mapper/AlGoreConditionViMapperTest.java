package ai.realworld.service.mapper;

import static ai.realworld.domain.AlGoreConditionViAsserts.*;
import static ai.realworld.domain.AlGoreConditionViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlGoreConditionViMapperTest {

    private AlGoreConditionViMapper alGoreConditionViMapper;

    @BeforeEach
    void setUp() {
        alGoreConditionViMapper = new AlGoreConditionViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlGoreConditionViSample1();
        var actual = alGoreConditionViMapper.toEntity(alGoreConditionViMapper.toDto(expected));
        assertAlGoreConditionViAllPropertiesEquals(expected, actual);
    }
}

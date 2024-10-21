package ai.realworld.service.mapper;

import static ai.realworld.domain.AlGoreConditionAsserts.*;
import static ai.realworld.domain.AlGoreConditionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlGoreConditionMapperTest {

    private AlGoreConditionMapper alGoreConditionMapper;

    @BeforeEach
    void setUp() {
        alGoreConditionMapper = new AlGoreConditionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlGoreConditionSample1();
        var actual = alGoreConditionMapper.toEntity(alGoreConditionMapper.toDto(expected));
        assertAlGoreConditionAllPropertiesEquals(expected, actual);
    }
}

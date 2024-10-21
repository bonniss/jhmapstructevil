package ai.realworld.service.mapper;

import static ai.realworld.domain.AlLadyGagaViAsserts.*;
import static ai.realworld.domain.AlLadyGagaViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlLadyGagaViMapperTest {

    private AlLadyGagaViMapper alLadyGagaViMapper;

    @BeforeEach
    void setUp() {
        alLadyGagaViMapper = new AlLadyGagaViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlLadyGagaViSample1();
        var actual = alLadyGagaViMapper.toEntity(alLadyGagaViMapper.toDto(expected));
        assertAlLadyGagaViAllPropertiesEquals(expected, actual);
    }
}

package ai.realworld.service.mapper;

import static ai.realworld.domain.AlLadyGagaAsserts.*;
import static ai.realworld.domain.AlLadyGagaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlLadyGagaMapperTest {

    private AlLadyGagaMapper alLadyGagaMapper;

    @BeforeEach
    void setUp() {
        alLadyGagaMapper = new AlLadyGagaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlLadyGagaSample1();
        var actual = alLadyGagaMapper.toEntity(alLadyGagaMapper.toDto(expected));
        assertAlLadyGagaAllPropertiesEquals(expected, actual);
    }
}

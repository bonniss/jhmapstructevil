package ai.realworld.service.mapper;

import static ai.realworld.domain.AlLexFergAsserts.*;
import static ai.realworld.domain.AlLexFergTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlLexFergMapperTest {

    private AlLexFergMapper alLexFergMapper;

    @BeforeEach
    void setUp() {
        alLexFergMapper = new AlLexFergMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlLexFergSample1();
        var actual = alLexFergMapper.toEntity(alLexFergMapper.toDto(expected));
        assertAlLexFergAllPropertiesEquals(expected, actual);
    }
}

package ai.realworld.service.mapper;

import static ai.realworld.domain.AlLexFergViAsserts.*;
import static ai.realworld.domain.AlLexFergViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlLexFergViMapperTest {

    private AlLexFergViMapper alLexFergViMapper;

    @BeforeEach
    void setUp() {
        alLexFergViMapper = new AlLexFergViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlLexFergViSample1();
        var actual = alLexFergViMapper.toEntity(alLexFergViMapper.toDto(expected));
        assertAlLexFergViAllPropertiesEquals(expected, actual);
    }
}

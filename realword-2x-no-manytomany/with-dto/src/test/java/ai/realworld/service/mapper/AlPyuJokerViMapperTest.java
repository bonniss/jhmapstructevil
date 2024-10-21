package ai.realworld.service.mapper;

import static ai.realworld.domain.AlPyuJokerViAsserts.*;
import static ai.realworld.domain.AlPyuJokerViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlPyuJokerViMapperTest {

    private AlPyuJokerViMapper alPyuJokerViMapper;

    @BeforeEach
    void setUp() {
        alPyuJokerViMapper = new AlPyuJokerViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlPyuJokerViSample1();
        var actual = alPyuJokerViMapper.toEntity(alPyuJokerViMapper.toDto(expected));
        assertAlPyuJokerViAllPropertiesEquals(expected, actual);
    }
}

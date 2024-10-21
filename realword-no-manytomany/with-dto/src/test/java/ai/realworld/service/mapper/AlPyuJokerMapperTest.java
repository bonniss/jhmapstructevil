package ai.realworld.service.mapper;

import static ai.realworld.domain.AlPyuJokerAsserts.*;
import static ai.realworld.domain.AlPyuJokerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlPyuJokerMapperTest {

    private AlPyuJokerMapper alPyuJokerMapper;

    @BeforeEach
    void setUp() {
        alPyuJokerMapper = new AlPyuJokerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlPyuJokerSample1();
        var actual = alPyuJokerMapper.toEntity(alPyuJokerMapper.toDto(expected));
        assertAlPyuJokerAllPropertiesEquals(expected, actual);
    }
}

package ai.realworld.service.mapper;

import static ai.realworld.domain.AlProtyAsserts.*;
import static ai.realworld.domain.AlProtyTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlProtyMapperTest {

    private AlProtyMapper alProtyMapper;

    @BeforeEach
    void setUp() {
        alProtyMapper = new AlProtyMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlProtySample1();
        var actual = alProtyMapper.toEntity(alProtyMapper.toDto(expected));
        assertAlProtyAllPropertiesEquals(expected, actual);
    }
}

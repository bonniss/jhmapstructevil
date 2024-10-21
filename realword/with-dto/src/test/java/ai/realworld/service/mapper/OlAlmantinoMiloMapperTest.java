package ai.realworld.service.mapper;

import static ai.realworld.domain.OlAlmantinoMiloAsserts.*;
import static ai.realworld.domain.OlAlmantinoMiloTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OlAlmantinoMiloMapperTest {

    private OlAlmantinoMiloMapper olAlmantinoMiloMapper;

    @BeforeEach
    void setUp() {
        olAlmantinoMiloMapper = new OlAlmantinoMiloMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOlAlmantinoMiloSample1();
        var actual = olAlmantinoMiloMapper.toEntity(olAlmantinoMiloMapper.toDto(expected));
        assertOlAlmantinoMiloAllPropertiesEquals(expected, actual);
    }
}

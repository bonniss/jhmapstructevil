package ai.realworld.service.mapper;

import static ai.realworld.domain.AlProProViAsserts.*;
import static ai.realworld.domain.AlProProViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlProProViMapperTest {

    private AlProProViMapper alProProViMapper;

    @BeforeEach
    void setUp() {
        alProProViMapper = new AlProProViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlProProViSample1();
        var actual = alProProViMapper.toEntity(alProProViMapper.toDto(expected));
        assertAlProProViAllPropertiesEquals(expected, actual);
    }
}

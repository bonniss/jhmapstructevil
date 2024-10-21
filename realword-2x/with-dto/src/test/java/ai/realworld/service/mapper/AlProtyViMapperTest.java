package ai.realworld.service.mapper;

import static ai.realworld.domain.AlProtyViAsserts.*;
import static ai.realworld.domain.AlProtyViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlProtyViMapperTest {

    private AlProtyViMapper alProtyViMapper;

    @BeforeEach
    void setUp() {
        alProtyViMapper = new AlProtyViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlProtyViSample1();
        var actual = alProtyViMapper.toEntity(alProtyViMapper.toDto(expected));
        assertAlProtyViAllPropertiesEquals(expected, actual);
    }
}

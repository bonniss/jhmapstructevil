package ai.realworld.service.mapper;

import static ai.realworld.domain.AlCatalinaViAsserts.*;
import static ai.realworld.domain.AlCatalinaViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlCatalinaViMapperTest {

    private AlCatalinaViMapper alCatalinaViMapper;

    @BeforeEach
    void setUp() {
        alCatalinaViMapper = new AlCatalinaViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlCatalinaViSample1();
        var actual = alCatalinaViMapper.toEntity(alCatalinaViMapper.toDto(expected));
        assertAlCatalinaViAllPropertiesEquals(expected, actual);
    }
}

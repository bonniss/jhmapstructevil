package ai.realworld.service.mapper;

import static ai.realworld.domain.AlDesireViAsserts.*;
import static ai.realworld.domain.AlDesireViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlDesireViMapperTest {

    private AlDesireViMapper alDesireViMapper;

    @BeforeEach
    void setUp() {
        alDesireViMapper = new AlDesireViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlDesireViSample1();
        var actual = alDesireViMapper.toEntity(alDesireViMapper.toDto(expected));
        assertAlDesireViAllPropertiesEquals(expected, actual);
    }
}

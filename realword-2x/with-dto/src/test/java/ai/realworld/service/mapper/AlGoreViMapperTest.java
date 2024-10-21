package ai.realworld.service.mapper;

import static ai.realworld.domain.AlGoreViAsserts.*;
import static ai.realworld.domain.AlGoreViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlGoreViMapperTest {

    private AlGoreViMapper alGoreViMapper;

    @BeforeEach
    void setUp() {
        alGoreViMapper = new AlGoreViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlGoreViSample1();
        var actual = alGoreViMapper.toEntity(alGoreViMapper.toDto(expected));
        assertAlGoreViAllPropertiesEquals(expected, actual);
    }
}

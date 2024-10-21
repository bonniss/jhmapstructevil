package ai.realworld.service.mapper;

import static ai.realworld.domain.AlZorroTemptationViAsserts.*;
import static ai.realworld.domain.AlZorroTemptationViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlZorroTemptationViMapperTest {

    private AlZorroTemptationViMapper alZorroTemptationViMapper;

    @BeforeEach
    void setUp() {
        alZorroTemptationViMapper = new AlZorroTemptationViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlZorroTemptationViSample1();
        var actual = alZorroTemptationViMapper.toEntity(alZorroTemptationViMapper.toDto(expected));
        assertAlZorroTemptationViAllPropertiesEquals(expected, actual);
    }
}

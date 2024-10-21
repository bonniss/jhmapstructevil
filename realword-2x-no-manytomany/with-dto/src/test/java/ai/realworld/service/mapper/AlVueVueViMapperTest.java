package ai.realworld.service.mapper;

import static ai.realworld.domain.AlVueVueViAsserts.*;
import static ai.realworld.domain.AlVueVueViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlVueVueViMapperTest {

    private AlVueVueViMapper alVueVueViMapper;

    @BeforeEach
    void setUp() {
        alVueVueViMapper = new AlVueVueViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlVueVueViSample1();
        var actual = alVueVueViMapper.toEntity(alVueVueViMapper.toDto(expected));
        assertAlVueVueViAllPropertiesEquals(expected, actual);
    }
}

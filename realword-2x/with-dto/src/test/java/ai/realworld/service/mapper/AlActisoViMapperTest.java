package ai.realworld.service.mapper;

import static ai.realworld.domain.AlActisoViAsserts.*;
import static ai.realworld.domain.AlActisoViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlActisoViMapperTest {

    private AlActisoViMapper alActisoViMapper;

    @BeforeEach
    void setUp() {
        alActisoViMapper = new AlActisoViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlActisoViSample1();
        var actual = alActisoViMapper.toEntity(alActisoViMapper.toDto(expected));
        assertAlActisoViAllPropertiesEquals(expected, actual);
    }
}

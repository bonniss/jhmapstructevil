package ai.realworld.service.mapper;

import static ai.realworld.domain.AlMemTierViAsserts.*;
import static ai.realworld.domain.AlMemTierViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlMemTierViMapperTest {

    private AlMemTierViMapper alMemTierViMapper;

    @BeforeEach
    void setUp() {
        alMemTierViMapper = new AlMemTierViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlMemTierViSample1();
        var actual = alMemTierViMapper.toEntity(alMemTierViMapper.toDto(expected));
        assertAlMemTierViAllPropertiesEquals(expected, actual);
    }
}

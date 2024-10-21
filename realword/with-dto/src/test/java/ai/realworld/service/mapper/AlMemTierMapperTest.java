package ai.realworld.service.mapper;

import static ai.realworld.domain.AlMemTierAsserts.*;
import static ai.realworld.domain.AlMemTierTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlMemTierMapperTest {

    private AlMemTierMapper alMemTierMapper;

    @BeforeEach
    void setUp() {
        alMemTierMapper = new AlMemTierMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlMemTierSample1();
        var actual = alMemTierMapper.toEntity(alMemTierMapper.toDto(expected));
        assertAlMemTierAllPropertiesEquals(expected, actual);
    }
}

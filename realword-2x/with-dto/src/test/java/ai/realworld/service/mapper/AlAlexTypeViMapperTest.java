package ai.realworld.service.mapper;

import static ai.realworld.domain.AlAlexTypeViAsserts.*;
import static ai.realworld.domain.AlAlexTypeViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlAlexTypeViMapperTest {

    private AlAlexTypeViMapper alAlexTypeViMapper;

    @BeforeEach
    void setUp() {
        alAlexTypeViMapper = new AlAlexTypeViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlAlexTypeViSample1();
        var actual = alAlexTypeViMapper.toEntity(alAlexTypeViMapper.toDto(expected));
        assertAlAlexTypeViAllPropertiesEquals(expected, actual);
    }
}

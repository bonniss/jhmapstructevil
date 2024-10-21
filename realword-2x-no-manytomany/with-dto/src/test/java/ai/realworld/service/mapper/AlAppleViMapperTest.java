package ai.realworld.service.mapper;

import static ai.realworld.domain.AlAppleViAsserts.*;
import static ai.realworld.domain.AlAppleViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlAppleViMapperTest {

    private AlAppleViMapper alAppleViMapper;

    @BeforeEach
    void setUp() {
        alAppleViMapper = new AlAppleViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlAppleViSample1();
        var actual = alAppleViMapper.toEntity(alAppleViMapper.toDto(expected));
        assertAlAppleViAllPropertiesEquals(expected, actual);
    }
}

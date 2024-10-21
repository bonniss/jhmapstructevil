package ai.realworld.service.mapper;

import static ai.realworld.domain.AlLeandroPlayingTimeViAsserts.*;
import static ai.realworld.domain.AlLeandroPlayingTimeViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlLeandroPlayingTimeViMapperTest {

    private AlLeandroPlayingTimeViMapper alLeandroPlayingTimeViMapper;

    @BeforeEach
    void setUp() {
        alLeandroPlayingTimeViMapper = new AlLeandroPlayingTimeViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlLeandroPlayingTimeViSample1();
        var actual = alLeandroPlayingTimeViMapper.toEntity(alLeandroPlayingTimeViMapper.toDto(expected));
        assertAlLeandroPlayingTimeViAllPropertiesEquals(expected, actual);
    }
}

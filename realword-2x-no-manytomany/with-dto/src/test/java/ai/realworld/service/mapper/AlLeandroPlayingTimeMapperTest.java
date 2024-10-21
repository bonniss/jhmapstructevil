package ai.realworld.service.mapper;

import static ai.realworld.domain.AlLeandroPlayingTimeAsserts.*;
import static ai.realworld.domain.AlLeandroPlayingTimeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlLeandroPlayingTimeMapperTest {

    private AlLeandroPlayingTimeMapper alLeandroPlayingTimeMapper;

    @BeforeEach
    void setUp() {
        alLeandroPlayingTimeMapper = new AlLeandroPlayingTimeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlLeandroPlayingTimeSample1();
        var actual = alLeandroPlayingTimeMapper.toEntity(alLeandroPlayingTimeMapper.toDto(expected));
        assertAlLeandroPlayingTimeAllPropertiesEquals(expected, actual);
    }
}

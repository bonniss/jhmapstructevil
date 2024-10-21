package ai.realworld.service.mapper;

import static ai.realworld.domain.AlPacinoPointHistoryViAsserts.*;
import static ai.realworld.domain.AlPacinoPointHistoryViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlPacinoPointHistoryViMapperTest {

    private AlPacinoPointHistoryViMapper alPacinoPointHistoryViMapper;

    @BeforeEach
    void setUp() {
        alPacinoPointHistoryViMapper = new AlPacinoPointHistoryViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlPacinoPointHistoryViSample1();
        var actual = alPacinoPointHistoryViMapper.toEntity(alPacinoPointHistoryViMapper.toDto(expected));
        assertAlPacinoPointHistoryViAllPropertiesEquals(expected, actual);
    }
}

package ai.realworld.service.mapper;

import static ai.realworld.domain.AlPacinoPointHistoryAsserts.*;
import static ai.realworld.domain.AlPacinoPointHistoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlPacinoPointHistoryMapperTest {

    private AlPacinoPointHistoryMapper alPacinoPointHistoryMapper;

    @BeforeEach
    void setUp() {
        alPacinoPointHistoryMapper = new AlPacinoPointHistoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlPacinoPointHistorySample1();
        var actual = alPacinoPointHistoryMapper.toEntity(alPacinoPointHistoryMapper.toDto(expected));
        assertAlPacinoPointHistoryAllPropertiesEquals(expected, actual);
    }
}

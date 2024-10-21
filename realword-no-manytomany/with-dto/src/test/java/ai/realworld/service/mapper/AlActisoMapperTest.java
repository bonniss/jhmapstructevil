package ai.realworld.service.mapper;

import static ai.realworld.domain.AlActisoAsserts.*;
import static ai.realworld.domain.AlActisoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlActisoMapperTest {

    private AlActisoMapper alActisoMapper;

    @BeforeEach
    void setUp() {
        alActisoMapper = new AlActisoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlActisoSample1();
        var actual = alActisoMapper.toEntity(alActisoMapper.toDto(expected));
        assertAlActisoAllPropertiesEquals(expected, actual);
    }
}

package ai.realworld.service.mapper;

import static ai.realworld.domain.AlLeandroAsserts.*;
import static ai.realworld.domain.AlLeandroTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlLeandroMapperTest {

    private AlLeandroMapper alLeandroMapper;

    @BeforeEach
    void setUp() {
        alLeandroMapper = new AlLeandroMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlLeandroSample1();
        var actual = alLeandroMapper.toEntity(alLeandroMapper.toDto(expected));
        assertAlLeandroAllPropertiesEquals(expected, actual);
    }
}

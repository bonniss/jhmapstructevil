package ai.realworld.service.mapper;

import static ai.realworld.domain.SaisanCogAsserts.*;
import static ai.realworld.domain.SaisanCogTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SaisanCogMapperTest {

    private SaisanCogMapper saisanCogMapper;

    @BeforeEach
    void setUp() {
        saisanCogMapper = new SaisanCogMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSaisanCogSample1();
        var actual = saisanCogMapper.toEntity(saisanCogMapper.toDto(expected));
        assertSaisanCogAllPropertiesEquals(expected, actual);
    }
}

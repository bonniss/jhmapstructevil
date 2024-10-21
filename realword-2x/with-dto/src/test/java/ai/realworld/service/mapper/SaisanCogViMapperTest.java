package ai.realworld.service.mapper;

import static ai.realworld.domain.SaisanCogViAsserts.*;
import static ai.realworld.domain.SaisanCogViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SaisanCogViMapperTest {

    private SaisanCogViMapper saisanCogViMapper;

    @BeforeEach
    void setUp() {
        saisanCogViMapper = new SaisanCogViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSaisanCogViSample1();
        var actual = saisanCogViMapper.toEntity(saisanCogViMapper.toDto(expected));
        assertSaisanCogViAllPropertiesEquals(expected, actual);
    }
}

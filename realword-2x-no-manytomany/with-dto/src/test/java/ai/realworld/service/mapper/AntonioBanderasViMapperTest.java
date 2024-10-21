package ai.realworld.service.mapper;

import static ai.realworld.domain.AntonioBanderasViAsserts.*;
import static ai.realworld.domain.AntonioBanderasViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AntonioBanderasViMapperTest {

    private AntonioBanderasViMapper antonioBanderasViMapper;

    @BeforeEach
    void setUp() {
        antonioBanderasViMapper = new AntonioBanderasViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAntonioBanderasViSample1();
        var actual = antonioBanderasViMapper.toEntity(antonioBanderasViMapper.toDto(expected));
        assertAntonioBanderasViAllPropertiesEquals(expected, actual);
    }
}

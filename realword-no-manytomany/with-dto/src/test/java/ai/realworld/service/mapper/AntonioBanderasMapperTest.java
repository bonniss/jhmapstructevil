package ai.realworld.service.mapper;

import static ai.realworld.domain.AntonioBanderasAsserts.*;
import static ai.realworld.domain.AntonioBanderasTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AntonioBanderasMapperTest {

    private AntonioBanderasMapper antonioBanderasMapper;

    @BeforeEach
    void setUp() {
        antonioBanderasMapper = new AntonioBanderasMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAntonioBanderasSample1();
        var actual = antonioBanderasMapper.toEntity(antonioBanderasMapper.toDto(expected));
        assertAntonioBanderasAllPropertiesEquals(expected, actual);
    }
}

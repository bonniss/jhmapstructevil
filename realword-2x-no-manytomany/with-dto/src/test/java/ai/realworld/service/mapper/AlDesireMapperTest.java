package ai.realworld.service.mapper;

import static ai.realworld.domain.AlDesireAsserts.*;
import static ai.realworld.domain.AlDesireTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlDesireMapperTest {

    private AlDesireMapper alDesireMapper;

    @BeforeEach
    void setUp() {
        alDesireMapper = new AlDesireMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlDesireSample1();
        var actual = alDesireMapper.toEntity(alDesireMapper.toDto(expected));
        assertAlDesireAllPropertiesEquals(expected, actual);
    }
}

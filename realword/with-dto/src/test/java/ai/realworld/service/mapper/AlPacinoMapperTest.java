package ai.realworld.service.mapper;

import static ai.realworld.domain.AlPacinoAsserts.*;
import static ai.realworld.domain.AlPacinoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlPacinoMapperTest {

    private AlPacinoMapper alPacinoMapper;

    @BeforeEach
    void setUp() {
        alPacinoMapper = new AlPacinoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlPacinoSample1();
        var actual = alPacinoMapper.toEntity(alPacinoMapper.toDto(expected));
        assertAlPacinoAllPropertiesEquals(expected, actual);
    }
}

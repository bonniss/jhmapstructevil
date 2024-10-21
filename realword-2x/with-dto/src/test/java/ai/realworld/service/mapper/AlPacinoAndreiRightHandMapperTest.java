package ai.realworld.service.mapper;

import static ai.realworld.domain.AlPacinoAndreiRightHandAsserts.*;
import static ai.realworld.domain.AlPacinoAndreiRightHandTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlPacinoAndreiRightHandMapperTest {

    private AlPacinoAndreiRightHandMapper alPacinoAndreiRightHandMapper;

    @BeforeEach
    void setUp() {
        alPacinoAndreiRightHandMapper = new AlPacinoAndreiRightHandMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlPacinoAndreiRightHandSample1();
        var actual = alPacinoAndreiRightHandMapper.toEntity(alPacinoAndreiRightHandMapper.toDto(expected));
        assertAlPacinoAndreiRightHandAllPropertiesEquals(expected, actual);
    }
}

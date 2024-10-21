package ai.realworld.service.mapper;

import static ai.realworld.domain.AlPacinoAndreiRightHandViAsserts.*;
import static ai.realworld.domain.AlPacinoAndreiRightHandViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlPacinoAndreiRightHandViMapperTest {

    private AlPacinoAndreiRightHandViMapper alPacinoAndreiRightHandViMapper;

    @BeforeEach
    void setUp() {
        alPacinoAndreiRightHandViMapper = new AlPacinoAndreiRightHandViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlPacinoAndreiRightHandViSample1();
        var actual = alPacinoAndreiRightHandViMapper.toEntity(alPacinoAndreiRightHandViMapper.toDto(expected));
        assertAlPacinoAndreiRightHandViAllPropertiesEquals(expected, actual);
    }
}

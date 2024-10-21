package ai.realworld.service.mapper;

import static ai.realworld.domain.AlProProAsserts.*;
import static ai.realworld.domain.AlProProTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlProProMapperTest {

    private AlProProMapper alProProMapper;

    @BeforeEach
    void setUp() {
        alProProMapper = new AlProProMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlProProSample1();
        var actual = alProProMapper.toEntity(alProProMapper.toDto(expected));
        assertAlProProAllPropertiesEquals(expected, actual);
    }
}

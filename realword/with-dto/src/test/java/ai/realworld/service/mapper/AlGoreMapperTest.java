package ai.realworld.service.mapper;

import static ai.realworld.domain.AlGoreAsserts.*;
import static ai.realworld.domain.AlGoreTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlGoreMapperTest {

    private AlGoreMapper alGoreMapper;

    @BeforeEach
    void setUp() {
        alGoreMapper = new AlGoreMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlGoreSample1();
        var actual = alGoreMapper.toEntity(alGoreMapper.toDto(expected));
        assertAlGoreAllPropertiesEquals(expected, actual);
    }
}

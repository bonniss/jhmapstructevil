package ai.realworld.service.mapper;

import static ai.realworld.domain.AlSherMaleAsserts.*;
import static ai.realworld.domain.AlSherMaleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlSherMaleMapperTest {

    private AlSherMaleMapper alSherMaleMapper;

    @BeforeEach
    void setUp() {
        alSherMaleMapper = new AlSherMaleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlSherMaleSample1();
        var actual = alSherMaleMapper.toEntity(alSherMaleMapper.toDto(expected));
        assertAlSherMaleAllPropertiesEquals(expected, actual);
    }
}

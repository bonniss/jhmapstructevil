package ai.realworld.service.mapper;

import static ai.realworld.domain.AlSherMaleViAsserts.*;
import static ai.realworld.domain.AlSherMaleViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlSherMaleViMapperTest {

    private AlSherMaleViMapper alSherMaleViMapper;

    @BeforeEach
    void setUp() {
        alSherMaleViMapper = new AlSherMaleViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlSherMaleViSample1();
        var actual = alSherMaleViMapper.toEntity(alSherMaleViMapper.toDto(expected));
        assertAlSherMaleViAllPropertiesEquals(expected, actual);
    }
}

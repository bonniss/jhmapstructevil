package ai.realworld.service.mapper;

import static ai.realworld.domain.AlPyuDjibrilViAsserts.*;
import static ai.realworld.domain.AlPyuDjibrilViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlPyuDjibrilViMapperTest {

    private AlPyuDjibrilViMapper alPyuDjibrilViMapper;

    @BeforeEach
    void setUp() {
        alPyuDjibrilViMapper = new AlPyuDjibrilViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlPyuDjibrilViSample1();
        var actual = alPyuDjibrilViMapper.toEntity(alPyuDjibrilViMapper.toDto(expected));
        assertAlPyuDjibrilViAllPropertiesEquals(expected, actual);
    }
}

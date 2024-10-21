package ai.realworld.service.mapper;

import static ai.realworld.domain.AlPyuThomasWayneViAsserts.*;
import static ai.realworld.domain.AlPyuThomasWayneViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlPyuThomasWayneViMapperTest {

    private AlPyuThomasWayneViMapper alPyuThomasWayneViMapper;

    @BeforeEach
    void setUp() {
        alPyuThomasWayneViMapper = new AlPyuThomasWayneViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlPyuThomasWayneViSample1();
        var actual = alPyuThomasWayneViMapper.toEntity(alPyuThomasWayneViMapper.toDto(expected));
        assertAlPyuThomasWayneViAllPropertiesEquals(expected, actual);
    }
}

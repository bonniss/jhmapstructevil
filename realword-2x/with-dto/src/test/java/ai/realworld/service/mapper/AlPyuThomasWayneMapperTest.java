package ai.realworld.service.mapper;

import static ai.realworld.domain.AlPyuThomasWayneAsserts.*;
import static ai.realworld.domain.AlPyuThomasWayneTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlPyuThomasWayneMapperTest {

    private AlPyuThomasWayneMapper alPyuThomasWayneMapper;

    @BeforeEach
    void setUp() {
        alPyuThomasWayneMapper = new AlPyuThomasWayneMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlPyuThomasWayneSample1();
        var actual = alPyuThomasWayneMapper.toEntity(alPyuThomasWayneMapper.toDto(expected));
        assertAlPyuThomasWayneAllPropertiesEquals(expected, actual);
    }
}

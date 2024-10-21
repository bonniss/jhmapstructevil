package ai.realworld.service.mapper;

import static ai.realworld.domain.AlPyuDjibrilAsserts.*;
import static ai.realworld.domain.AlPyuDjibrilTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlPyuDjibrilMapperTest {

    private AlPyuDjibrilMapper alPyuDjibrilMapper;

    @BeforeEach
    void setUp() {
        alPyuDjibrilMapper = new AlPyuDjibrilMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlPyuDjibrilSample1();
        var actual = alPyuDjibrilMapper.toEntity(alPyuDjibrilMapper.toDto(expected));
        assertAlPyuDjibrilAllPropertiesEquals(expected, actual);
    }
}

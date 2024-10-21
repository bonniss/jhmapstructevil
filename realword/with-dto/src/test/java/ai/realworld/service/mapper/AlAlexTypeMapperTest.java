package ai.realworld.service.mapper;

import static ai.realworld.domain.AlAlexTypeAsserts.*;
import static ai.realworld.domain.AlAlexTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlAlexTypeMapperTest {

    private AlAlexTypeMapper alAlexTypeMapper;

    @BeforeEach
    void setUp() {
        alAlexTypeMapper = new AlAlexTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlAlexTypeSample1();
        var actual = alAlexTypeMapper.toEntity(alAlexTypeMapper.toDto(expected));
        assertAlAlexTypeAllPropertiesEquals(expected, actual);
    }
}

package ai.realworld.service.mapper;

import static ai.realworld.domain.AlCatalinaAsserts.*;
import static ai.realworld.domain.AlCatalinaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlCatalinaMapperTest {

    private AlCatalinaMapper alCatalinaMapper;

    @BeforeEach
    void setUp() {
        alCatalinaMapper = new AlCatalinaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlCatalinaSample1();
        var actual = alCatalinaMapper.toEntity(alCatalinaMapper.toDto(expected));
        assertAlCatalinaAllPropertiesEquals(expected, actual);
    }
}

package ai.realworld.service.mapper;

import static ai.realworld.domain.PamelaLouisAsserts.*;
import static ai.realworld.domain.PamelaLouisTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PamelaLouisMapperTest {

    private PamelaLouisMapper pamelaLouisMapper;

    @BeforeEach
    void setUp() {
        pamelaLouisMapper = new PamelaLouisMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPamelaLouisSample1();
        var actual = pamelaLouisMapper.toEntity(pamelaLouisMapper.toDto(expected));
        assertPamelaLouisAllPropertiesEquals(expected, actual);
    }
}

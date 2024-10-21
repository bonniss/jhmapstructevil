package ai.realworld.service.mapper;

import static ai.realworld.domain.AlMenityAsserts.*;
import static ai.realworld.domain.AlMenityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlMenityMapperTest {

    private AlMenityMapper alMenityMapper;

    @BeforeEach
    void setUp() {
        alMenityMapper = new AlMenityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlMenitySample1();
        var actual = alMenityMapper.toEntity(alMenityMapper.toDto(expected));
        assertAlMenityAllPropertiesEquals(expected, actual);
    }
}

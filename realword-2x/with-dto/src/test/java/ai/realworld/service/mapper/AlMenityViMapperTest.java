package ai.realworld.service.mapper;

import static ai.realworld.domain.AlMenityViAsserts.*;
import static ai.realworld.domain.AlMenityViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlMenityViMapperTest {

    private AlMenityViMapper alMenityViMapper;

    @BeforeEach
    void setUp() {
        alMenityViMapper = new AlMenityViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlMenityViSample1();
        var actual = alMenityViMapper.toEntity(alMenityViMapper.toDto(expected));
        assertAlMenityViAllPropertiesEquals(expected, actual);
    }
}

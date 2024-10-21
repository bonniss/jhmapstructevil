package ai.realworld.service.mapper;

import static ai.realworld.domain.AlZorroTemptationAsserts.*;
import static ai.realworld.domain.AlZorroTemptationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlZorroTemptationMapperTest {

    private AlZorroTemptationMapper alZorroTemptationMapper;

    @BeforeEach
    void setUp() {
        alZorroTemptationMapper = new AlZorroTemptationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlZorroTemptationSample1();
        var actual = alZorroTemptationMapper.toEntity(alZorroTemptationMapper.toDto(expected));
        assertAlZorroTemptationAllPropertiesEquals(expected, actual);
    }
}

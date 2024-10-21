package ai.realworld.service.mapper;

import static ai.realworld.domain.AlVueVueViUsageAsserts.*;
import static ai.realworld.domain.AlVueVueViUsageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlVueVueViUsageMapperTest {

    private AlVueVueViUsageMapper alVueVueViUsageMapper;

    @BeforeEach
    void setUp() {
        alVueVueViUsageMapper = new AlVueVueViUsageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlVueVueViUsageSample1();
        var actual = alVueVueViUsageMapper.toEntity(alVueVueViUsageMapper.toDto(expected));
        assertAlVueVueViUsageAllPropertiesEquals(expected, actual);
    }
}

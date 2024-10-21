package ai.realworld.service.mapper;

import static ai.realworld.domain.AlVueVueUsageAsserts.*;
import static ai.realworld.domain.AlVueVueUsageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlVueVueUsageMapperTest {

    private AlVueVueUsageMapper alVueVueUsageMapper;

    @BeforeEach
    void setUp() {
        alVueVueUsageMapper = new AlVueVueUsageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlVueVueUsageSample1();
        var actual = alVueVueUsageMapper.toEntity(alVueVueUsageMapper.toDto(expected));
        assertAlVueVueUsageAllPropertiesEquals(expected, actual);
    }
}

package ai.realworld.service.mapper;

import static ai.realworld.domain.AlVueVueViConditionAsserts.*;
import static ai.realworld.domain.AlVueVueViConditionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlVueVueViConditionMapperTest {

    private AlVueVueViConditionMapper alVueVueViConditionMapper;

    @BeforeEach
    void setUp() {
        alVueVueViConditionMapper = new AlVueVueViConditionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlVueVueViConditionSample1();
        var actual = alVueVueViConditionMapper.toEntity(alVueVueViConditionMapper.toDto(expected));
        assertAlVueVueViConditionAllPropertiesEquals(expected, actual);
    }
}

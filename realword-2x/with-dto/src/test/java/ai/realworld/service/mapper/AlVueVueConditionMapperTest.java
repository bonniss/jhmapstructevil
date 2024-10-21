package ai.realworld.service.mapper;

import static ai.realworld.domain.AlVueVueConditionAsserts.*;
import static ai.realworld.domain.AlVueVueConditionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlVueVueConditionMapperTest {

    private AlVueVueConditionMapper alVueVueConditionMapper;

    @BeforeEach
    void setUp() {
        alVueVueConditionMapper = new AlVueVueConditionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlVueVueConditionSample1();
        var actual = alVueVueConditionMapper.toEntity(alVueVueConditionMapper.toDto(expected));
        assertAlVueVueConditionAllPropertiesEquals(expected, actual);
    }
}

package ai.realworld.service.mapper;

import static ai.realworld.domain.AlVueVueAsserts.*;
import static ai.realworld.domain.AlVueVueTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlVueVueMapperTest {

    private AlVueVueMapper alVueVueMapper;

    @BeforeEach
    void setUp() {
        alVueVueMapper = new AlVueVueMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlVueVueSample1();
        var actual = alVueVueMapper.toEntity(alVueVueMapper.toDto(expected));
        assertAlVueVueAllPropertiesEquals(expected, actual);
    }
}

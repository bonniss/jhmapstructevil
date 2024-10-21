package ai.realworld.service.mapper;

import static ai.realworld.domain.AlPounderAsserts.*;
import static ai.realworld.domain.AlPounderTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlPounderMapperTest {

    private AlPounderMapper alPounderMapper;

    @BeforeEach
    void setUp() {
        alPounderMapper = new AlPounderMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlPounderSample1();
        var actual = alPounderMapper.toEntity(alPounderMapper.toDto(expected));
        assertAlPounderAllPropertiesEquals(expected, actual);
    }
}

package ai.realworld.service.mapper;

import static ai.realworld.domain.AlPounderViAsserts.*;
import static ai.realworld.domain.AlPounderViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlPounderViMapperTest {

    private AlPounderViMapper alPounderViMapper;

    @BeforeEach
    void setUp() {
        alPounderViMapper = new AlPounderViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlPounderViSample1();
        var actual = alPounderViMapper.toEntity(alPounderViMapper.toDto(expected));
        assertAlPounderViAllPropertiesEquals(expected, actual);
    }
}

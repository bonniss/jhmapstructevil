package ai.realworld.service.mapper;

import static ai.realworld.domain.AlBestToothViAsserts.*;
import static ai.realworld.domain.AlBestToothViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlBestToothViMapperTest {

    private AlBestToothViMapper alBestToothViMapper;

    @BeforeEach
    void setUp() {
        alBestToothViMapper = new AlBestToothViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlBestToothViSample1();
        var actual = alBestToothViMapper.toEntity(alBestToothViMapper.toDto(expected));
        assertAlBestToothViAllPropertiesEquals(expected, actual);
    }
}

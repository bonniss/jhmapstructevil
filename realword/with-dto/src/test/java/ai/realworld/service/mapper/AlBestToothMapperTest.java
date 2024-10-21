package ai.realworld.service.mapper;

import static ai.realworld.domain.AlBestToothAsserts.*;
import static ai.realworld.domain.AlBestToothTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlBestToothMapperTest {

    private AlBestToothMapper alBestToothMapper;

    @BeforeEach
    void setUp() {
        alBestToothMapper = new AlBestToothMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlBestToothSample1();
        var actual = alBestToothMapper.toEntity(alBestToothMapper.toDto(expected));
        assertAlBestToothAllPropertiesEquals(expected, actual);
    }
}

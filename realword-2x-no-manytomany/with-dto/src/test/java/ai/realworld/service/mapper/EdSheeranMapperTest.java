package ai.realworld.service.mapper;

import static ai.realworld.domain.EdSheeranAsserts.*;
import static ai.realworld.domain.EdSheeranTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EdSheeranMapperTest {

    private EdSheeranMapper edSheeranMapper;

    @BeforeEach
    void setUp() {
        edSheeranMapper = new EdSheeranMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEdSheeranSample1();
        var actual = edSheeranMapper.toEntity(edSheeranMapper.toDto(expected));
        assertEdSheeranAllPropertiesEquals(expected, actual);
    }
}

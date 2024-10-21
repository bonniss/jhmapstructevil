package ai.realworld.service.mapper;

import static ai.realworld.domain.HashRossAsserts.*;
import static ai.realworld.domain.HashRossTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HashRossMapperTest {

    private HashRossMapper hashRossMapper;

    @BeforeEach
    void setUp() {
        hashRossMapper = new HashRossMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHashRossSample1();
        var actual = hashRossMapper.toEntity(hashRossMapper.toDto(expected));
        assertHashRossAllPropertiesEquals(expected, actual);
    }
}

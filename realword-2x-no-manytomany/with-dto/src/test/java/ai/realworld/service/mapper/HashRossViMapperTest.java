package ai.realworld.service.mapper;

import static ai.realworld.domain.HashRossViAsserts.*;
import static ai.realworld.domain.HashRossViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HashRossViMapperTest {

    private HashRossViMapper hashRossViMapper;

    @BeforeEach
    void setUp() {
        hashRossViMapper = new HashRossViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHashRossViSample1();
        var actual = hashRossViMapper.toEntity(hashRossViMapper.toDto(expected));
        assertHashRossViAllPropertiesEquals(expected, actual);
    }
}

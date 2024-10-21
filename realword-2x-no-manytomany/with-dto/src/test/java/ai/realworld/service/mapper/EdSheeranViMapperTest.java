package ai.realworld.service.mapper;

import static ai.realworld.domain.EdSheeranViAsserts.*;
import static ai.realworld.domain.EdSheeranViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EdSheeranViMapperTest {

    private EdSheeranViMapper edSheeranViMapper;

    @BeforeEach
    void setUp() {
        edSheeranViMapper = new EdSheeranViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEdSheeranViSample1();
        var actual = edSheeranViMapper.toEntity(edSheeranViMapper.toDto(expected));
        assertEdSheeranViAllPropertiesEquals(expected, actual);
    }
}

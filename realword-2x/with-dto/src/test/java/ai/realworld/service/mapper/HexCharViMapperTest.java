package ai.realworld.service.mapper;

import static ai.realworld.domain.HexCharViAsserts.*;
import static ai.realworld.domain.HexCharViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HexCharViMapperTest {

    private HexCharViMapper hexCharViMapper;

    @BeforeEach
    void setUp() {
        hexCharViMapper = new HexCharViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHexCharViSample1();
        var actual = hexCharViMapper.toEntity(hexCharViMapper.toDto(expected));
        assertHexCharViAllPropertiesEquals(expected, actual);
    }
}

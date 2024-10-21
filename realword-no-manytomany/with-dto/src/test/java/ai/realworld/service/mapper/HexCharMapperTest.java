package ai.realworld.service.mapper;

import static ai.realworld.domain.HexCharAsserts.*;
import static ai.realworld.domain.HexCharTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HexCharMapperTest {

    private HexCharMapper hexCharMapper;

    @BeforeEach
    void setUp() {
        hexCharMapper = new HexCharMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHexCharSample1();
        var actual = hexCharMapper.toEntity(hexCharMapper.toDto(expected));
        assertHexCharAllPropertiesEquals(expected, actual);
    }
}

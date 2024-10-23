package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextProductMiMiAsserts.*;
import static xyz.jhmapstruct.domain.NextProductMiMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextProductMiMiMapperTest {

    private NextProductMiMiMapper nextProductMiMiMapper;

    @BeforeEach
    void setUp() {
        nextProductMiMiMapper = new NextProductMiMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextProductMiMiSample1();
        var actual = nextProductMiMiMapper.toEntity(nextProductMiMiMapper.toDto(expected));
        assertNextProductMiMiAllPropertiesEquals(expected, actual);
    }
}

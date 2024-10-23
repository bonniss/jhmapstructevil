package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextProductMiAsserts.*;
import static xyz.jhmapstruct.domain.NextProductMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextProductMiMapperTest {

    private NextProductMiMapper nextProductMiMapper;

    @BeforeEach
    void setUp() {
        nextProductMiMapper = new NextProductMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextProductMiSample1();
        var actual = nextProductMiMapper.toEntity(nextProductMiMapper.toDto(expected));
        assertNextProductMiAllPropertiesEquals(expected, actual);
    }
}

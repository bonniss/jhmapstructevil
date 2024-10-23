package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextProductAsserts.*;
import static xyz.jhmapstruct.domain.NextProductTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextProductMapperTest {

    private NextProductMapper nextProductMapper;

    @BeforeEach
    void setUp() {
        nextProductMapper = new NextProductMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextProductSample1();
        var actual = nextProductMapper.toEntity(nextProductMapper.toDto(expected));
        assertNextProductAllPropertiesEquals(expected, actual);
    }
}

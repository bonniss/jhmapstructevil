package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextProductSigmaAsserts.*;
import static xyz.jhmapstruct.domain.NextProductSigmaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextProductSigmaMapperTest {

    private NextProductSigmaMapper nextProductSigmaMapper;

    @BeforeEach
    void setUp() {
        nextProductSigmaMapper = new NextProductSigmaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextProductSigmaSample1();
        var actual = nextProductSigmaMapper.toEntity(nextProductSigmaMapper.toDto(expected));
        assertNextProductSigmaAllPropertiesEquals(expected, actual);
    }
}

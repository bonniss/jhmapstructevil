package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextProductBetaAsserts.*;
import static xyz.jhmapstruct.domain.NextProductBetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextProductBetaMapperTest {

    private NextProductBetaMapper nextProductBetaMapper;

    @BeforeEach
    void setUp() {
        nextProductBetaMapper = new NextProductBetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextProductBetaSample1();
        var actual = nextProductBetaMapper.toEntity(nextProductBetaMapper.toDto(expected));
        assertNextProductBetaAllPropertiesEquals(expected, actual);
    }
}

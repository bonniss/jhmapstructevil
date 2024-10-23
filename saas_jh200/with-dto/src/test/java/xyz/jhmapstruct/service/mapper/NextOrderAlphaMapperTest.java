package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextOrderAlphaAsserts.*;
import static xyz.jhmapstruct.domain.NextOrderAlphaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextOrderAlphaMapperTest {

    private NextOrderAlphaMapper nextOrderAlphaMapper;

    @BeforeEach
    void setUp() {
        nextOrderAlphaMapper = new NextOrderAlphaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextOrderAlphaSample1();
        var actual = nextOrderAlphaMapper.toEntity(nextOrderAlphaMapper.toDto(expected));
        assertNextOrderAlphaAllPropertiesEquals(expected, actual);
    }
}

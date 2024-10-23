package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextOrderBetaAsserts.*;
import static xyz.jhmapstruct.domain.NextOrderBetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextOrderBetaMapperTest {

    private NextOrderBetaMapper nextOrderBetaMapper;

    @BeforeEach
    void setUp() {
        nextOrderBetaMapper = new NextOrderBetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextOrderBetaSample1();
        var actual = nextOrderBetaMapper.toEntity(nextOrderBetaMapper.toDto(expected));
        assertNextOrderBetaAllPropertiesEquals(expected, actual);
    }
}

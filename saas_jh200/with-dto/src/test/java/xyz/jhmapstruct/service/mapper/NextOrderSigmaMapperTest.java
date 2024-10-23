package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextOrderSigmaAsserts.*;
import static xyz.jhmapstruct.domain.NextOrderSigmaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextOrderSigmaMapperTest {

    private NextOrderSigmaMapper nextOrderSigmaMapper;

    @BeforeEach
    void setUp() {
        nextOrderSigmaMapper = new NextOrderSigmaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextOrderSigmaSample1();
        var actual = nextOrderSigmaMapper.toEntity(nextOrderSigmaMapper.toDto(expected));
        assertNextOrderSigmaAllPropertiesEquals(expected, actual);
    }
}

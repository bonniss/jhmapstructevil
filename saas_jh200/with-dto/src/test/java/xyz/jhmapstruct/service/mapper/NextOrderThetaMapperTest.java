package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextOrderThetaAsserts.*;
import static xyz.jhmapstruct.domain.NextOrderThetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextOrderThetaMapperTest {

    private NextOrderThetaMapper nextOrderThetaMapper;

    @BeforeEach
    void setUp() {
        nextOrderThetaMapper = new NextOrderThetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextOrderThetaSample1();
        var actual = nextOrderThetaMapper.toEntity(nextOrderThetaMapper.toDto(expected));
        assertNextOrderThetaAllPropertiesEquals(expected, actual);
    }
}

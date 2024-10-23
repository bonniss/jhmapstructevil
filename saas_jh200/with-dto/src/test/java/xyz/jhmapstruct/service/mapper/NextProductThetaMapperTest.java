package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextProductThetaAsserts.*;
import static xyz.jhmapstruct.domain.NextProductThetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextProductThetaMapperTest {

    private NextProductThetaMapper nextProductThetaMapper;

    @BeforeEach
    void setUp() {
        nextProductThetaMapper = new NextProductThetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextProductThetaSample1();
        var actual = nextProductThetaMapper.toEntity(nextProductThetaMapper.toDto(expected));
        assertNextProductThetaAllPropertiesEquals(expected, actual);
    }
}

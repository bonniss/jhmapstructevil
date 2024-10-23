package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextCustomerThetaAsserts.*;
import static xyz.jhmapstruct.domain.NextCustomerThetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextCustomerThetaMapperTest {

    private NextCustomerThetaMapper nextCustomerThetaMapper;

    @BeforeEach
    void setUp() {
        nextCustomerThetaMapper = new NextCustomerThetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextCustomerThetaSample1();
        var actual = nextCustomerThetaMapper.toEntity(nextCustomerThetaMapper.toDto(expected));
        assertNextCustomerThetaAllPropertiesEquals(expected, actual);
    }
}

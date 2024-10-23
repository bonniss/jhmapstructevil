package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextCustomerBetaAsserts.*;
import static xyz.jhmapstruct.domain.NextCustomerBetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextCustomerBetaMapperTest {

    private NextCustomerBetaMapper nextCustomerBetaMapper;

    @BeforeEach
    void setUp() {
        nextCustomerBetaMapper = new NextCustomerBetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextCustomerBetaSample1();
        var actual = nextCustomerBetaMapper.toEntity(nextCustomerBetaMapper.toDto(expected));
        assertNextCustomerBetaAllPropertiesEquals(expected, actual);
    }
}

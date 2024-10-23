package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextCustomerSigmaAsserts.*;
import static xyz.jhmapstruct.domain.NextCustomerSigmaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextCustomerSigmaMapperTest {

    private NextCustomerSigmaMapper nextCustomerSigmaMapper;

    @BeforeEach
    void setUp() {
        nextCustomerSigmaMapper = new NextCustomerSigmaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextCustomerSigmaSample1();
        var actual = nextCustomerSigmaMapper.toEntity(nextCustomerSigmaMapper.toDto(expected));
        assertNextCustomerSigmaAllPropertiesEquals(expected, actual);
    }
}

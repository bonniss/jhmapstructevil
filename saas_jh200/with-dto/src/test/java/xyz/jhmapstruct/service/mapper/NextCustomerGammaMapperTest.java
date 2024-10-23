package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextCustomerGammaAsserts.*;
import static xyz.jhmapstruct.domain.NextCustomerGammaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextCustomerGammaMapperTest {

    private NextCustomerGammaMapper nextCustomerGammaMapper;

    @BeforeEach
    void setUp() {
        nextCustomerGammaMapper = new NextCustomerGammaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextCustomerGammaSample1();
        var actual = nextCustomerGammaMapper.toEntity(nextCustomerGammaMapper.toDto(expected));
        assertNextCustomerGammaAllPropertiesEquals(expected, actual);
    }
}

package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextCustomerAlphaAsserts.*;
import static xyz.jhmapstruct.domain.NextCustomerAlphaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextCustomerAlphaMapperTest {

    private NextCustomerAlphaMapper nextCustomerAlphaMapper;

    @BeforeEach
    void setUp() {
        nextCustomerAlphaMapper = new NextCustomerAlphaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextCustomerAlphaSample1();
        var actual = nextCustomerAlphaMapper.toEntity(nextCustomerAlphaMapper.toDto(expected));
        assertNextCustomerAlphaAllPropertiesEquals(expected, actual);
    }
}

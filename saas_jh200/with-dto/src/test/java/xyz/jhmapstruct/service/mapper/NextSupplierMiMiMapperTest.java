package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextSupplierMiMiAsserts.*;
import static xyz.jhmapstruct.domain.NextSupplierMiMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextSupplierMiMiMapperTest {

    private NextSupplierMiMiMapper nextSupplierMiMiMapper;

    @BeforeEach
    void setUp() {
        nextSupplierMiMiMapper = new NextSupplierMiMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextSupplierMiMiSample1();
        var actual = nextSupplierMiMiMapper.toEntity(nextSupplierMiMiMapper.toDto(expected));
        assertNextSupplierMiMiAllPropertiesEquals(expected, actual);
    }
}

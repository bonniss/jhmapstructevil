package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextSupplierMiAsserts.*;
import static xyz.jhmapstruct.domain.NextSupplierMiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextSupplierMiMapperTest {

    private NextSupplierMiMapper nextSupplierMiMapper;

    @BeforeEach
    void setUp() {
        nextSupplierMiMapper = new NextSupplierMiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextSupplierMiSample1();
        var actual = nextSupplierMiMapper.toEntity(nextSupplierMiMapper.toDto(expected));
        assertNextSupplierMiAllPropertiesEquals(expected, actual);
    }
}

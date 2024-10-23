package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextSupplierAsserts.*;
import static xyz.jhmapstruct.domain.NextSupplierTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextSupplierMapperTest {

    private NextSupplierMapper nextSupplierMapper;

    @BeforeEach
    void setUp() {
        nextSupplierMapper = new NextSupplierMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextSupplierSample1();
        var actual = nextSupplierMapper.toEntity(nextSupplierMapper.toDto(expected));
        assertNextSupplierAllPropertiesEquals(expected, actual);
    }
}

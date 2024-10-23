package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextSupplierViAsserts.*;
import static xyz.jhmapstruct.domain.NextSupplierViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextSupplierViMapperTest {

    private NextSupplierViMapper nextSupplierViMapper;

    @BeforeEach
    void setUp() {
        nextSupplierViMapper = new NextSupplierViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextSupplierViSample1();
        var actual = nextSupplierViMapper.toEntity(nextSupplierViMapper.toDto(expected));
        assertNextSupplierViAllPropertiesEquals(expected, actual);
    }
}

package xyz.jhmapstruct.service.mapper;

import static xyz.jhmapstruct.domain.NextSupplierViViAsserts.*;
import static xyz.jhmapstruct.domain.NextSupplierViViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextSupplierViViMapperTest {

    private NextSupplierViViMapper nextSupplierViViMapper;

    @BeforeEach
    void setUp() {
        nextSupplierViViMapper = new NextSupplierViViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNextSupplierViViSample1();
        var actual = nextSupplierViViMapper.toEntity(nextSupplierViViMapper.toDto(expected));
        assertNextSupplierViViAllPropertiesEquals(expected, actual);
    }
}

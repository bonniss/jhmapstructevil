package ai.realworld.service.mapper;

import static ai.realworld.domain.AlPedroTaxViAsserts.*;
import static ai.realworld.domain.AlPedroTaxViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlPedroTaxViMapperTest {

    private AlPedroTaxViMapper alPedroTaxViMapper;

    @BeforeEach
    void setUp() {
        alPedroTaxViMapper = new AlPedroTaxViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlPedroTaxViSample1();
        var actual = alPedroTaxViMapper.toEntity(alPedroTaxViMapper.toDto(expected));
        assertAlPedroTaxViAllPropertiesEquals(expected, actual);
    }
}

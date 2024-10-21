package ai.realworld.service.mapper;

import static ai.realworld.domain.AlPedroTaxAsserts.*;
import static ai.realworld.domain.AlPedroTaxTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlPedroTaxMapperTest {

    private AlPedroTaxMapper alPedroTaxMapper;

    @BeforeEach
    void setUp() {
        alPedroTaxMapper = new AlPedroTaxMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlPedroTaxSample1();
        var actual = alPedroTaxMapper.toEntity(alPedroTaxMapper.toDto(expected));
        assertAlPedroTaxAllPropertiesEquals(expected, actual);
    }
}

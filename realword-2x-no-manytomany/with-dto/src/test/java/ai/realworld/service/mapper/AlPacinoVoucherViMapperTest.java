package ai.realworld.service.mapper;

import static ai.realworld.domain.AlPacinoVoucherViAsserts.*;
import static ai.realworld.domain.AlPacinoVoucherViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlPacinoVoucherViMapperTest {

    private AlPacinoVoucherViMapper alPacinoVoucherViMapper;

    @BeforeEach
    void setUp() {
        alPacinoVoucherViMapper = new AlPacinoVoucherViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlPacinoVoucherViSample1();
        var actual = alPacinoVoucherViMapper.toEntity(alPacinoVoucherViMapper.toDto(expected));
        assertAlPacinoVoucherViAllPropertiesEquals(expected, actual);
    }
}

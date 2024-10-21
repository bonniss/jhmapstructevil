package ai.realworld.service.mapper;

import static ai.realworld.domain.AlPacinoVoucherAsserts.*;
import static ai.realworld.domain.AlPacinoVoucherTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlPacinoVoucherMapperTest {

    private AlPacinoVoucherMapper alPacinoVoucherMapper;

    @BeforeEach
    void setUp() {
        alPacinoVoucherMapper = new AlPacinoVoucherMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlPacinoVoucherSample1();
        var actual = alPacinoVoucherMapper.toEntity(alPacinoVoucherMapper.toDto(expected));
        assertAlPacinoVoucherAllPropertiesEquals(expected, actual);
    }
}

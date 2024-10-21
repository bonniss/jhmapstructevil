package ai.realworld.service.mapper;

import static ai.realworld.domain.AlPowerShellAsserts.*;
import static ai.realworld.domain.AlPowerShellTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlPowerShellMapperTest {

    private AlPowerShellMapper alPowerShellMapper;

    @BeforeEach
    void setUp() {
        alPowerShellMapper = new AlPowerShellMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlPowerShellSample1();
        var actual = alPowerShellMapper.toEntity(alPowerShellMapper.toDto(expected));
        assertAlPowerShellAllPropertiesEquals(expected, actual);
    }
}

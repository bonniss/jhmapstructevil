package ai.realworld.service.mapper;

import static ai.realworld.domain.AlPowerShellViAsserts.*;
import static ai.realworld.domain.AlPowerShellViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlPowerShellViMapperTest {

    private AlPowerShellViMapper alPowerShellViMapper;

    @BeforeEach
    void setUp() {
        alPowerShellViMapper = new AlPowerShellViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlPowerShellViSample1();
        var actual = alPowerShellViMapper.toEntity(alPowerShellViMapper.toDto(expected));
        assertAlPowerShellViAllPropertiesEquals(expected, actual);
    }
}

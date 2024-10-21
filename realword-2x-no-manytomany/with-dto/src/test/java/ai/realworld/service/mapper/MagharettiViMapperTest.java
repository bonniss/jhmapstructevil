package ai.realworld.service.mapper;

import static ai.realworld.domain.MagharettiViAsserts.*;
import static ai.realworld.domain.MagharettiViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MagharettiViMapperTest {

    private MagharettiViMapper magharettiViMapper;

    @BeforeEach
    void setUp() {
        magharettiViMapper = new MagharettiViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMagharettiViSample1();
        var actual = magharettiViMapper.toEntity(magharettiViMapper.toDto(expected));
        assertMagharettiViAllPropertiesEquals(expected, actual);
    }
}

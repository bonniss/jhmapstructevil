package ai.realworld.service.mapper;

import static ai.realworld.domain.MagharettiAsserts.*;
import static ai.realworld.domain.MagharettiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MagharettiMapperTest {

    private MagharettiMapper magharettiMapper;

    @BeforeEach
    void setUp() {
        magharettiMapper = new MagharettiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMagharettiSample1();
        var actual = magharettiMapper.toEntity(magharettiMapper.toDto(expected));
        assertMagharettiAllPropertiesEquals(expected, actual);
    }
}

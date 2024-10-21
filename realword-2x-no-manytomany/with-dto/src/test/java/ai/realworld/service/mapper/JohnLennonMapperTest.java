package ai.realworld.service.mapper;

import static ai.realworld.domain.JohnLennonAsserts.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JohnLennonMapperTest {

    private JohnLennonMapper johnLennonMapper;

    @BeforeEach
    void setUp() {
        johnLennonMapper = new JohnLennonMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getJohnLennonSample1();
        var actual = johnLennonMapper.toEntity(johnLennonMapper.toDto(expected));
        assertJohnLennonAllPropertiesEquals(expected, actual);
    }
}

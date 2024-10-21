package ai.realworld.service.mapper;

import static ai.realworld.domain.InitiumViAsserts.*;
import static ai.realworld.domain.InitiumViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InitiumViMapperTest {

    private InitiumViMapper initiumViMapper;

    @BeforeEach
    void setUp() {
        initiumViMapper = new InitiumViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInitiumViSample1();
        var actual = initiumViMapper.toEntity(initiumViMapper.toDto(expected));
        assertInitiumViAllPropertiesEquals(expected, actual);
    }
}

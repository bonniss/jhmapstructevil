package ai.realworld.service.mapper;

import static ai.realworld.domain.InitiumAsserts.*;
import static ai.realworld.domain.InitiumTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InitiumMapperTest {

    private InitiumMapper initiumMapper;

    @BeforeEach
    void setUp() {
        initiumMapper = new InitiumMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInitiumSample1();
        var actual = initiumMapper.toEntity(initiumMapper.toDto(expected));
        assertInitiumAllPropertiesEquals(expected, actual);
    }
}

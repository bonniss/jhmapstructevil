package ai.realworld.service.mapper;

import static ai.realworld.domain.AndreiRightHandViAsserts.*;
import static ai.realworld.domain.AndreiRightHandViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AndreiRightHandViMapperTest {

    private AndreiRightHandViMapper andreiRightHandViMapper;

    @BeforeEach
    void setUp() {
        andreiRightHandViMapper = new AndreiRightHandViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAndreiRightHandViSample1();
        var actual = andreiRightHandViMapper.toEntity(andreiRightHandViMapper.toDto(expected));
        assertAndreiRightHandViAllPropertiesEquals(expected, actual);
    }
}

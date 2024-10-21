package ai.realworld.service.mapper;

import static ai.realworld.domain.AndreiRightHandAsserts.*;
import static ai.realworld.domain.AndreiRightHandTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AndreiRightHandMapperTest {

    private AndreiRightHandMapper andreiRightHandMapper;

    @BeforeEach
    void setUp() {
        andreiRightHandMapper = new AndreiRightHandMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAndreiRightHandSample1();
        var actual = andreiRightHandMapper.toEntity(andreiRightHandMapper.toDto(expected));
        assertAndreiRightHandAllPropertiesEquals(expected, actual);
    }
}

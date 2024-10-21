package ai.realworld.service.mapper;

import static ai.realworld.domain.HandCraftAsserts.*;
import static ai.realworld.domain.HandCraftTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HandCraftMapperTest {

    private HandCraftMapper handCraftMapper;

    @BeforeEach
    void setUp() {
        handCraftMapper = new HandCraftMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHandCraftSample1();
        var actual = handCraftMapper.toEntity(handCraftMapper.toDto(expected));
        assertHandCraftAllPropertiesEquals(expected, actual);
    }
}

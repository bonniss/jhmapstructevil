package ai.realworld.service.mapper;

import static ai.realworld.domain.HandCraftViAsserts.*;
import static ai.realworld.domain.HandCraftViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HandCraftViMapperTest {

    private HandCraftViMapper handCraftViMapper;

    @BeforeEach
    void setUp() {
        handCraftViMapper = new HandCraftViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHandCraftViSample1();
        var actual = handCraftViMapper.toEntity(handCraftViMapper.toDto(expected));
        assertHandCraftViAllPropertiesEquals(expected, actual);
    }
}

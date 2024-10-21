package ai.realworld.service.mapper;

import static ai.realworld.domain.AllMassageThaiViAsserts.*;
import static ai.realworld.domain.AllMassageThaiViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AllMassageThaiViMapperTest {

    private AllMassageThaiViMapper allMassageThaiViMapper;

    @BeforeEach
    void setUp() {
        allMassageThaiViMapper = new AllMassageThaiViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAllMassageThaiViSample1();
        var actual = allMassageThaiViMapper.toEntity(allMassageThaiViMapper.toDto(expected));
        assertAllMassageThaiViAllPropertiesEquals(expected, actual);
    }
}

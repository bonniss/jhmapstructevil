package ai.realworld.service.mapper;

import static ai.realworld.domain.AllMassageThaiAsserts.*;
import static ai.realworld.domain.AllMassageThaiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AllMassageThaiMapperTest {

    private AllMassageThaiMapper allMassageThaiMapper;

    @BeforeEach
    void setUp() {
        allMassageThaiMapper = new AllMassageThaiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAllMassageThaiSample1();
        var actual = allMassageThaiMapper.toEntity(allMassageThaiMapper.toDto(expected));
        assertAllMassageThaiAllPropertiesEquals(expected, actual);
    }
}

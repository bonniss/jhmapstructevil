package ai.realworld.service.mapper;

import static ai.realworld.domain.AlAppleAsserts.*;
import static ai.realworld.domain.AlAppleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlAppleMapperTest {

    private AlAppleMapper alAppleMapper;

    @BeforeEach
    void setUp() {
        alAppleMapper = new AlAppleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlAppleSample1();
        var actual = alAppleMapper.toEntity(alAppleMapper.toDto(expected));
        assertAlAppleAllPropertiesEquals(expected, actual);
    }
}

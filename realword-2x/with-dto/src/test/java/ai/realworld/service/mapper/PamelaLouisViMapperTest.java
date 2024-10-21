package ai.realworld.service.mapper;

import static ai.realworld.domain.PamelaLouisViAsserts.*;
import static ai.realworld.domain.PamelaLouisViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PamelaLouisViMapperTest {

    private PamelaLouisViMapper pamelaLouisViMapper;

    @BeforeEach
    void setUp() {
        pamelaLouisViMapper = new PamelaLouisViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPamelaLouisViSample1();
        var actual = pamelaLouisViMapper.toEntity(pamelaLouisViMapper.toDto(expected));
        assertPamelaLouisViAllPropertiesEquals(expected, actual);
    }
}

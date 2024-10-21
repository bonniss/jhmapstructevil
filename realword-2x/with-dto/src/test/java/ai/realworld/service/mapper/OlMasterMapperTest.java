package ai.realworld.service.mapper;

import static ai.realworld.domain.OlMasterAsserts.*;
import static ai.realworld.domain.OlMasterTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OlMasterMapperTest {

    private OlMasterMapper olMasterMapper;

    @BeforeEach
    void setUp() {
        olMasterMapper = new OlMasterMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOlMasterSample1();
        var actual = olMasterMapper.toEntity(olMasterMapper.toDto(expected));
        assertOlMasterAllPropertiesEquals(expected, actual);
    }
}

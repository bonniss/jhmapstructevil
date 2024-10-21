package ai.realworld.service.mapper;

import static ai.realworld.domain.RihannaAsserts.*;
import static ai.realworld.domain.RihannaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RihannaMapperTest {

    private RihannaMapper rihannaMapper;

    @BeforeEach
    void setUp() {
        rihannaMapper = new RihannaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRihannaSample1();
        var actual = rihannaMapper.toEntity(rihannaMapper.toDto(expected));
        assertRihannaAllPropertiesEquals(expected, actual);
    }
}

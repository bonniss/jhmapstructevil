package ai.realworld.service.mapper;

import static ai.realworld.domain.RihannaViAsserts.*;
import static ai.realworld.domain.RihannaViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RihannaViMapperTest {

    private RihannaViMapper rihannaViMapper;

    @BeforeEach
    void setUp() {
        rihannaViMapper = new RihannaViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRihannaViSample1();
        var actual = rihannaViMapper.toEntity(rihannaViMapper.toDto(expected));
        assertRihannaViAllPropertiesEquals(expected, actual);
    }
}

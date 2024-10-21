package ai.realworld.service.mapper;

import static ai.realworld.domain.SicilyUmetoAsserts.*;
import static ai.realworld.domain.SicilyUmetoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SicilyUmetoMapperTest {

    private SicilyUmetoMapper sicilyUmetoMapper;

    @BeforeEach
    void setUp() {
        sicilyUmetoMapper = new SicilyUmetoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSicilyUmetoSample1();
        var actual = sicilyUmetoMapper.toEntity(sicilyUmetoMapper.toDto(expected));
        assertSicilyUmetoAllPropertiesEquals(expected, actual);
    }
}

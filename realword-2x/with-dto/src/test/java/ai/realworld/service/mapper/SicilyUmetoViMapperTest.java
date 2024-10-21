package ai.realworld.service.mapper;

import static ai.realworld.domain.SicilyUmetoViAsserts.*;
import static ai.realworld.domain.SicilyUmetoViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SicilyUmetoViMapperTest {

    private SicilyUmetoViMapper sicilyUmetoViMapper;

    @BeforeEach
    void setUp() {
        sicilyUmetoViMapper = new SicilyUmetoViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSicilyUmetoViSample1();
        var actual = sicilyUmetoViMapper.toEntity(sicilyUmetoViMapper.toDto(expected));
        assertSicilyUmetoViAllPropertiesEquals(expected, actual);
    }
}

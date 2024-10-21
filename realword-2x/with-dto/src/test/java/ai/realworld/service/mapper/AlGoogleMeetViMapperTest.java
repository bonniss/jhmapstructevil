package ai.realworld.service.mapper;

import static ai.realworld.domain.AlGoogleMeetViAsserts.*;
import static ai.realworld.domain.AlGoogleMeetViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlGoogleMeetViMapperTest {

    private AlGoogleMeetViMapper alGoogleMeetViMapper;

    @BeforeEach
    void setUp() {
        alGoogleMeetViMapper = new AlGoogleMeetViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlGoogleMeetViSample1();
        var actual = alGoogleMeetViMapper.toEntity(alGoogleMeetViMapper.toDto(expected));
        assertAlGoogleMeetViAllPropertiesEquals(expected, actual);
    }
}

package ai.realworld.service.mapper;

import static ai.realworld.domain.AlGoogleMeetAsserts.*;
import static ai.realworld.domain.AlGoogleMeetTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlGoogleMeetMapperTest {

    private AlGoogleMeetMapper alGoogleMeetMapper;

    @BeforeEach
    void setUp() {
        alGoogleMeetMapper = new AlGoogleMeetMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlGoogleMeetSample1();
        var actual = alGoogleMeetMapper.toEntity(alGoogleMeetMapper.toDto(expected));
        assertAlGoogleMeetAllPropertiesEquals(expected, actual);
    }
}

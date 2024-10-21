package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlGoogleMeetViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlGoogleMeetViDTO.class);
        AlGoogleMeetViDTO alGoogleMeetViDTO1 = new AlGoogleMeetViDTO();
        alGoogleMeetViDTO1.setId(UUID.randomUUID());
        AlGoogleMeetViDTO alGoogleMeetViDTO2 = new AlGoogleMeetViDTO();
        assertThat(alGoogleMeetViDTO1).isNotEqualTo(alGoogleMeetViDTO2);
        alGoogleMeetViDTO2.setId(alGoogleMeetViDTO1.getId());
        assertThat(alGoogleMeetViDTO1).isEqualTo(alGoogleMeetViDTO2);
        alGoogleMeetViDTO2.setId(UUID.randomUUID());
        assertThat(alGoogleMeetViDTO1).isNotEqualTo(alGoogleMeetViDTO2);
        alGoogleMeetViDTO1.setId(null);
        assertThat(alGoogleMeetViDTO1).isNotEqualTo(alGoogleMeetViDTO2);
    }
}

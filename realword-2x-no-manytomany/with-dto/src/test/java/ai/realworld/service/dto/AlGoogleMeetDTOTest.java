package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlGoogleMeetDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlGoogleMeetDTO.class);
        AlGoogleMeetDTO alGoogleMeetDTO1 = new AlGoogleMeetDTO();
        alGoogleMeetDTO1.setId(UUID.randomUUID());
        AlGoogleMeetDTO alGoogleMeetDTO2 = new AlGoogleMeetDTO();
        assertThat(alGoogleMeetDTO1).isNotEqualTo(alGoogleMeetDTO2);
        alGoogleMeetDTO2.setId(alGoogleMeetDTO1.getId());
        assertThat(alGoogleMeetDTO1).isEqualTo(alGoogleMeetDTO2);
        alGoogleMeetDTO2.setId(UUID.randomUUID());
        assertThat(alGoogleMeetDTO1).isNotEqualTo(alGoogleMeetDTO2);
        alGoogleMeetDTO1.setId(null);
        assertThat(alGoogleMeetDTO1).isNotEqualTo(alGoogleMeetDTO2);
    }
}

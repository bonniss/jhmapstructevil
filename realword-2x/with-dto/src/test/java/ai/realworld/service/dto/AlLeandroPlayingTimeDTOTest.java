package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlLeandroPlayingTimeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlLeandroPlayingTimeDTO.class);
        AlLeandroPlayingTimeDTO alLeandroPlayingTimeDTO1 = new AlLeandroPlayingTimeDTO();
        alLeandroPlayingTimeDTO1.setId(UUID.randomUUID());
        AlLeandroPlayingTimeDTO alLeandroPlayingTimeDTO2 = new AlLeandroPlayingTimeDTO();
        assertThat(alLeandroPlayingTimeDTO1).isNotEqualTo(alLeandroPlayingTimeDTO2);
        alLeandroPlayingTimeDTO2.setId(alLeandroPlayingTimeDTO1.getId());
        assertThat(alLeandroPlayingTimeDTO1).isEqualTo(alLeandroPlayingTimeDTO2);
        alLeandroPlayingTimeDTO2.setId(UUID.randomUUID());
        assertThat(alLeandroPlayingTimeDTO1).isNotEqualTo(alLeandroPlayingTimeDTO2);
        alLeandroPlayingTimeDTO1.setId(null);
        assertThat(alLeandroPlayingTimeDTO1).isNotEqualTo(alLeandroPlayingTimeDTO2);
    }
}

package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlLeandroPlayingTimeViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlLeandroPlayingTimeViDTO.class);
        AlLeandroPlayingTimeViDTO alLeandroPlayingTimeViDTO1 = new AlLeandroPlayingTimeViDTO();
        alLeandroPlayingTimeViDTO1.setId(UUID.randomUUID());
        AlLeandroPlayingTimeViDTO alLeandroPlayingTimeViDTO2 = new AlLeandroPlayingTimeViDTO();
        assertThat(alLeandroPlayingTimeViDTO1).isNotEqualTo(alLeandroPlayingTimeViDTO2);
        alLeandroPlayingTimeViDTO2.setId(alLeandroPlayingTimeViDTO1.getId());
        assertThat(alLeandroPlayingTimeViDTO1).isEqualTo(alLeandroPlayingTimeViDTO2);
        alLeandroPlayingTimeViDTO2.setId(UUID.randomUUID());
        assertThat(alLeandroPlayingTimeViDTO1).isNotEqualTo(alLeandroPlayingTimeViDTO2);
        alLeandroPlayingTimeViDTO1.setId(null);
        assertThat(alLeandroPlayingTimeViDTO1).isNotEqualTo(alLeandroPlayingTimeViDTO2);
    }
}

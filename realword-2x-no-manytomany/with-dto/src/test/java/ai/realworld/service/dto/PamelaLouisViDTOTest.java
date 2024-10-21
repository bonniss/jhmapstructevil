package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PamelaLouisViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PamelaLouisViDTO.class);
        PamelaLouisViDTO pamelaLouisViDTO1 = new PamelaLouisViDTO();
        pamelaLouisViDTO1.setId(1L);
        PamelaLouisViDTO pamelaLouisViDTO2 = new PamelaLouisViDTO();
        assertThat(pamelaLouisViDTO1).isNotEqualTo(pamelaLouisViDTO2);
        pamelaLouisViDTO2.setId(pamelaLouisViDTO1.getId());
        assertThat(pamelaLouisViDTO1).isEqualTo(pamelaLouisViDTO2);
        pamelaLouisViDTO2.setId(2L);
        assertThat(pamelaLouisViDTO1).isNotEqualTo(pamelaLouisViDTO2);
        pamelaLouisViDTO1.setId(null);
        assertThat(pamelaLouisViDTO1).isNotEqualTo(pamelaLouisViDTO2);
    }
}

package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PamelaLouisDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PamelaLouisDTO.class);
        PamelaLouisDTO pamelaLouisDTO1 = new PamelaLouisDTO();
        pamelaLouisDTO1.setId(1L);
        PamelaLouisDTO pamelaLouisDTO2 = new PamelaLouisDTO();
        assertThat(pamelaLouisDTO1).isNotEqualTo(pamelaLouisDTO2);
        pamelaLouisDTO2.setId(pamelaLouisDTO1.getId());
        assertThat(pamelaLouisDTO1).isEqualTo(pamelaLouisDTO2);
        pamelaLouisDTO2.setId(2L);
        assertThat(pamelaLouisDTO1).isNotEqualTo(pamelaLouisDTO2);
        pamelaLouisDTO1.setId(null);
        assertThat(pamelaLouisDTO1).isNotEqualTo(pamelaLouisDTO2);
    }
}

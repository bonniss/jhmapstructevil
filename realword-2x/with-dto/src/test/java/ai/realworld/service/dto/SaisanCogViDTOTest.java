package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SaisanCogViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaisanCogViDTO.class);
        SaisanCogViDTO saisanCogViDTO1 = new SaisanCogViDTO();
        saisanCogViDTO1.setId(1L);
        SaisanCogViDTO saisanCogViDTO2 = new SaisanCogViDTO();
        assertThat(saisanCogViDTO1).isNotEqualTo(saisanCogViDTO2);
        saisanCogViDTO2.setId(saisanCogViDTO1.getId());
        assertThat(saisanCogViDTO1).isEqualTo(saisanCogViDTO2);
        saisanCogViDTO2.setId(2L);
        assertThat(saisanCogViDTO1).isNotEqualTo(saisanCogViDTO2);
        saisanCogViDTO1.setId(null);
        assertThat(saisanCogViDTO1).isNotEqualTo(saisanCogViDTO2);
    }
}

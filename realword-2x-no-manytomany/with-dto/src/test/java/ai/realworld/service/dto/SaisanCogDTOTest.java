package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SaisanCogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaisanCogDTO.class);
        SaisanCogDTO saisanCogDTO1 = new SaisanCogDTO();
        saisanCogDTO1.setId(1L);
        SaisanCogDTO saisanCogDTO2 = new SaisanCogDTO();
        assertThat(saisanCogDTO1).isNotEqualTo(saisanCogDTO2);
        saisanCogDTO2.setId(saisanCogDTO1.getId());
        assertThat(saisanCogDTO1).isEqualTo(saisanCogDTO2);
        saisanCogDTO2.setId(2L);
        assertThat(saisanCogDTO1).isNotEqualTo(saisanCogDTO2);
        saisanCogDTO1.setId(null);
        assertThat(saisanCogDTO1).isNotEqualTo(saisanCogDTO2);
    }
}

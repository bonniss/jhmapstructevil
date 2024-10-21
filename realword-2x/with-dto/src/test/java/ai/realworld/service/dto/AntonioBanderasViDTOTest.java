package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AntonioBanderasViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AntonioBanderasViDTO.class);
        AntonioBanderasViDTO antonioBanderasViDTO1 = new AntonioBanderasViDTO();
        antonioBanderasViDTO1.setId(1L);
        AntonioBanderasViDTO antonioBanderasViDTO2 = new AntonioBanderasViDTO();
        assertThat(antonioBanderasViDTO1).isNotEqualTo(antonioBanderasViDTO2);
        antonioBanderasViDTO2.setId(antonioBanderasViDTO1.getId());
        assertThat(antonioBanderasViDTO1).isEqualTo(antonioBanderasViDTO2);
        antonioBanderasViDTO2.setId(2L);
        assertThat(antonioBanderasViDTO1).isNotEqualTo(antonioBanderasViDTO2);
        antonioBanderasViDTO1.setId(null);
        assertThat(antonioBanderasViDTO1).isNotEqualTo(antonioBanderasViDTO2);
    }
}

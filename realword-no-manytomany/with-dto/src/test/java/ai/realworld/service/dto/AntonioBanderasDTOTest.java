package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AntonioBanderasDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AntonioBanderasDTO.class);
        AntonioBanderasDTO antonioBanderasDTO1 = new AntonioBanderasDTO();
        antonioBanderasDTO1.setId(1L);
        AntonioBanderasDTO antonioBanderasDTO2 = new AntonioBanderasDTO();
        assertThat(antonioBanderasDTO1).isNotEqualTo(antonioBanderasDTO2);
        antonioBanderasDTO2.setId(antonioBanderasDTO1.getId());
        assertThat(antonioBanderasDTO1).isEqualTo(antonioBanderasDTO2);
        antonioBanderasDTO2.setId(2L);
        assertThat(antonioBanderasDTO1).isNotEqualTo(antonioBanderasDTO2);
        antonioBanderasDTO1.setId(null);
        assertThat(antonioBanderasDTO1).isNotEqualTo(antonioBanderasDTO2);
    }
}

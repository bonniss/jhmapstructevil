package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlSherMaleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlSherMaleDTO.class);
        AlSherMaleDTO alSherMaleDTO1 = new AlSherMaleDTO();
        alSherMaleDTO1.setId(1L);
        AlSherMaleDTO alSherMaleDTO2 = new AlSherMaleDTO();
        assertThat(alSherMaleDTO1).isNotEqualTo(alSherMaleDTO2);
        alSherMaleDTO2.setId(alSherMaleDTO1.getId());
        assertThat(alSherMaleDTO1).isEqualTo(alSherMaleDTO2);
        alSherMaleDTO2.setId(2L);
        assertThat(alSherMaleDTO1).isNotEqualTo(alSherMaleDTO2);
        alSherMaleDTO1.setId(null);
        assertThat(alSherMaleDTO1).isNotEqualTo(alSherMaleDTO2);
    }
}

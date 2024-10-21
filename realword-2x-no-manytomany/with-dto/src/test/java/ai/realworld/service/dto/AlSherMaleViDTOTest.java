package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlSherMaleViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlSherMaleViDTO.class);
        AlSherMaleViDTO alSherMaleViDTO1 = new AlSherMaleViDTO();
        alSherMaleViDTO1.setId(1L);
        AlSherMaleViDTO alSherMaleViDTO2 = new AlSherMaleViDTO();
        assertThat(alSherMaleViDTO1).isNotEqualTo(alSherMaleViDTO2);
        alSherMaleViDTO2.setId(alSherMaleViDTO1.getId());
        assertThat(alSherMaleViDTO1).isEqualTo(alSherMaleViDTO2);
        alSherMaleViDTO2.setId(2L);
        assertThat(alSherMaleViDTO1).isNotEqualTo(alSherMaleViDTO2);
        alSherMaleViDTO1.setId(null);
        assertThat(alSherMaleViDTO1).isNotEqualTo(alSherMaleViDTO2);
    }
}

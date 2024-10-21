package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlMenityViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlMenityViDTO.class);
        AlMenityViDTO alMenityViDTO1 = new AlMenityViDTO();
        alMenityViDTO1.setId(1L);
        AlMenityViDTO alMenityViDTO2 = new AlMenityViDTO();
        assertThat(alMenityViDTO1).isNotEqualTo(alMenityViDTO2);
        alMenityViDTO2.setId(alMenityViDTO1.getId());
        assertThat(alMenityViDTO1).isEqualTo(alMenityViDTO2);
        alMenityViDTO2.setId(2L);
        assertThat(alMenityViDTO1).isNotEqualTo(alMenityViDTO2);
        alMenityViDTO1.setId(null);
        assertThat(alMenityViDTO1).isNotEqualTo(alMenityViDTO2);
    }
}

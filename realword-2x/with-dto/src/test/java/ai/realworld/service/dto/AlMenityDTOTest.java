package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlMenityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlMenityDTO.class);
        AlMenityDTO alMenityDTO1 = new AlMenityDTO();
        alMenityDTO1.setId(1L);
        AlMenityDTO alMenityDTO2 = new AlMenityDTO();
        assertThat(alMenityDTO1).isNotEqualTo(alMenityDTO2);
        alMenityDTO2.setId(alMenityDTO1.getId());
        assertThat(alMenityDTO1).isEqualTo(alMenityDTO2);
        alMenityDTO2.setId(2L);
        assertThat(alMenityDTO1).isNotEqualTo(alMenityDTO2);
        alMenityDTO1.setId(null);
        assertThat(alMenityDTO1).isNotEqualTo(alMenityDTO2);
    }
}

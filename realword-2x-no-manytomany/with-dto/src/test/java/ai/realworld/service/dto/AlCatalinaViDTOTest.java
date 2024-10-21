package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlCatalinaViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlCatalinaViDTO.class);
        AlCatalinaViDTO alCatalinaViDTO1 = new AlCatalinaViDTO();
        alCatalinaViDTO1.setId(1L);
        AlCatalinaViDTO alCatalinaViDTO2 = new AlCatalinaViDTO();
        assertThat(alCatalinaViDTO1).isNotEqualTo(alCatalinaViDTO2);
        alCatalinaViDTO2.setId(alCatalinaViDTO1.getId());
        assertThat(alCatalinaViDTO1).isEqualTo(alCatalinaViDTO2);
        alCatalinaViDTO2.setId(2L);
        assertThat(alCatalinaViDTO1).isNotEqualTo(alCatalinaViDTO2);
        alCatalinaViDTO1.setId(null);
        assertThat(alCatalinaViDTO1).isNotEqualTo(alCatalinaViDTO2);
    }
}

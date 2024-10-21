package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlGoreConditionViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlGoreConditionViDTO.class);
        AlGoreConditionViDTO alGoreConditionViDTO1 = new AlGoreConditionViDTO();
        alGoreConditionViDTO1.setId(1L);
        AlGoreConditionViDTO alGoreConditionViDTO2 = new AlGoreConditionViDTO();
        assertThat(alGoreConditionViDTO1).isNotEqualTo(alGoreConditionViDTO2);
        alGoreConditionViDTO2.setId(alGoreConditionViDTO1.getId());
        assertThat(alGoreConditionViDTO1).isEqualTo(alGoreConditionViDTO2);
        alGoreConditionViDTO2.setId(2L);
        assertThat(alGoreConditionViDTO1).isNotEqualTo(alGoreConditionViDTO2);
        alGoreConditionViDTO1.setId(null);
        assertThat(alGoreConditionViDTO1).isNotEqualTo(alGoreConditionViDTO2);
    }
}

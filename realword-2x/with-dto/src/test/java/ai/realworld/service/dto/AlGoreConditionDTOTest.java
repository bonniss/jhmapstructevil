package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlGoreConditionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlGoreConditionDTO.class);
        AlGoreConditionDTO alGoreConditionDTO1 = new AlGoreConditionDTO();
        alGoreConditionDTO1.setId(1L);
        AlGoreConditionDTO alGoreConditionDTO2 = new AlGoreConditionDTO();
        assertThat(alGoreConditionDTO1).isNotEqualTo(alGoreConditionDTO2);
        alGoreConditionDTO2.setId(alGoreConditionDTO1.getId());
        assertThat(alGoreConditionDTO1).isEqualTo(alGoreConditionDTO2);
        alGoreConditionDTO2.setId(2L);
        assertThat(alGoreConditionDTO1).isNotEqualTo(alGoreConditionDTO2);
        alGoreConditionDTO1.setId(null);
        assertThat(alGoreConditionDTO1).isNotEqualTo(alGoreConditionDTO2);
    }
}

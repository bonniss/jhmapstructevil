package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlVueVueConditionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlVueVueConditionDTO.class);
        AlVueVueConditionDTO alVueVueConditionDTO1 = new AlVueVueConditionDTO();
        alVueVueConditionDTO1.setId(1L);
        AlVueVueConditionDTO alVueVueConditionDTO2 = new AlVueVueConditionDTO();
        assertThat(alVueVueConditionDTO1).isNotEqualTo(alVueVueConditionDTO2);
        alVueVueConditionDTO2.setId(alVueVueConditionDTO1.getId());
        assertThat(alVueVueConditionDTO1).isEqualTo(alVueVueConditionDTO2);
        alVueVueConditionDTO2.setId(2L);
        assertThat(alVueVueConditionDTO1).isNotEqualTo(alVueVueConditionDTO2);
        alVueVueConditionDTO1.setId(null);
        assertThat(alVueVueConditionDTO1).isNotEqualTo(alVueVueConditionDTO2);
    }
}

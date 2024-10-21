package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlVueVueViConditionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlVueVueViConditionDTO.class);
        AlVueVueViConditionDTO alVueVueViConditionDTO1 = new AlVueVueViConditionDTO();
        alVueVueViConditionDTO1.setId(1L);
        AlVueVueViConditionDTO alVueVueViConditionDTO2 = new AlVueVueViConditionDTO();
        assertThat(alVueVueViConditionDTO1).isNotEqualTo(alVueVueViConditionDTO2);
        alVueVueViConditionDTO2.setId(alVueVueViConditionDTO1.getId());
        assertThat(alVueVueViConditionDTO1).isEqualTo(alVueVueViConditionDTO2);
        alVueVueViConditionDTO2.setId(2L);
        assertThat(alVueVueViConditionDTO1).isNotEqualTo(alVueVueViConditionDTO2);
        alVueVueViConditionDTO1.setId(null);
        assertThat(alVueVueViConditionDTO1).isNotEqualTo(alVueVueViConditionDTO2);
    }
}

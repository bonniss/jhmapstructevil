package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AndreiRightHandDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AndreiRightHandDTO.class);
        AndreiRightHandDTO andreiRightHandDTO1 = new AndreiRightHandDTO();
        andreiRightHandDTO1.setId(1L);
        AndreiRightHandDTO andreiRightHandDTO2 = new AndreiRightHandDTO();
        assertThat(andreiRightHandDTO1).isNotEqualTo(andreiRightHandDTO2);
        andreiRightHandDTO2.setId(andreiRightHandDTO1.getId());
        assertThat(andreiRightHandDTO1).isEqualTo(andreiRightHandDTO2);
        andreiRightHandDTO2.setId(2L);
        assertThat(andreiRightHandDTO1).isNotEqualTo(andreiRightHandDTO2);
        andreiRightHandDTO1.setId(null);
        assertThat(andreiRightHandDTO1).isNotEqualTo(andreiRightHandDTO2);
    }
}

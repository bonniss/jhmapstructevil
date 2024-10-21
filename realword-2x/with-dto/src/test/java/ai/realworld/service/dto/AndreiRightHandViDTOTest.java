package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AndreiRightHandViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AndreiRightHandViDTO.class);
        AndreiRightHandViDTO andreiRightHandViDTO1 = new AndreiRightHandViDTO();
        andreiRightHandViDTO1.setId(1L);
        AndreiRightHandViDTO andreiRightHandViDTO2 = new AndreiRightHandViDTO();
        assertThat(andreiRightHandViDTO1).isNotEqualTo(andreiRightHandViDTO2);
        andreiRightHandViDTO2.setId(andreiRightHandViDTO1.getId());
        assertThat(andreiRightHandViDTO1).isEqualTo(andreiRightHandViDTO2);
        andreiRightHandViDTO2.setId(2L);
        assertThat(andreiRightHandViDTO1).isNotEqualTo(andreiRightHandViDTO2);
        andreiRightHandViDTO1.setId(null);
        assertThat(andreiRightHandViDTO1).isNotEqualTo(andreiRightHandViDTO2);
    }
}

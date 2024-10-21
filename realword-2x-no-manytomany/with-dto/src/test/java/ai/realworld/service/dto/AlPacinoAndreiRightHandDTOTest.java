package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPacinoAndreiRightHandDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPacinoAndreiRightHandDTO.class);
        AlPacinoAndreiRightHandDTO alPacinoAndreiRightHandDTO1 = new AlPacinoAndreiRightHandDTO();
        alPacinoAndreiRightHandDTO1.setId(1L);
        AlPacinoAndreiRightHandDTO alPacinoAndreiRightHandDTO2 = new AlPacinoAndreiRightHandDTO();
        assertThat(alPacinoAndreiRightHandDTO1).isNotEqualTo(alPacinoAndreiRightHandDTO2);
        alPacinoAndreiRightHandDTO2.setId(alPacinoAndreiRightHandDTO1.getId());
        assertThat(alPacinoAndreiRightHandDTO1).isEqualTo(alPacinoAndreiRightHandDTO2);
        alPacinoAndreiRightHandDTO2.setId(2L);
        assertThat(alPacinoAndreiRightHandDTO1).isNotEqualTo(alPacinoAndreiRightHandDTO2);
        alPacinoAndreiRightHandDTO1.setId(null);
        assertThat(alPacinoAndreiRightHandDTO1).isNotEqualTo(alPacinoAndreiRightHandDTO2);
    }
}

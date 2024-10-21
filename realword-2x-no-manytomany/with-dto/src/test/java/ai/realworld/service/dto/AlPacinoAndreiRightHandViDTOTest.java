package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPacinoAndreiRightHandViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPacinoAndreiRightHandViDTO.class);
        AlPacinoAndreiRightHandViDTO alPacinoAndreiRightHandViDTO1 = new AlPacinoAndreiRightHandViDTO();
        alPacinoAndreiRightHandViDTO1.setId(1L);
        AlPacinoAndreiRightHandViDTO alPacinoAndreiRightHandViDTO2 = new AlPacinoAndreiRightHandViDTO();
        assertThat(alPacinoAndreiRightHandViDTO1).isNotEqualTo(alPacinoAndreiRightHandViDTO2);
        alPacinoAndreiRightHandViDTO2.setId(alPacinoAndreiRightHandViDTO1.getId());
        assertThat(alPacinoAndreiRightHandViDTO1).isEqualTo(alPacinoAndreiRightHandViDTO2);
        alPacinoAndreiRightHandViDTO2.setId(2L);
        assertThat(alPacinoAndreiRightHandViDTO1).isNotEqualTo(alPacinoAndreiRightHandViDTO2);
        alPacinoAndreiRightHandViDTO1.setId(null);
        assertThat(alPacinoAndreiRightHandViDTO1).isNotEqualTo(alPacinoAndreiRightHandViDTO2);
    }
}

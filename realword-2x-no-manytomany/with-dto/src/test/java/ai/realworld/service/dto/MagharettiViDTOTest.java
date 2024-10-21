package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MagharettiViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MagharettiViDTO.class);
        MagharettiViDTO magharettiViDTO1 = new MagharettiViDTO();
        magharettiViDTO1.setId(1L);
        MagharettiViDTO magharettiViDTO2 = new MagharettiViDTO();
        assertThat(magharettiViDTO1).isNotEqualTo(magharettiViDTO2);
        magharettiViDTO2.setId(magharettiViDTO1.getId());
        assertThat(magharettiViDTO1).isEqualTo(magharettiViDTO2);
        magharettiViDTO2.setId(2L);
        assertThat(magharettiViDTO1).isNotEqualTo(magharettiViDTO2);
        magharettiViDTO1.setId(null);
        assertThat(magharettiViDTO1).isNotEqualTo(magharettiViDTO2);
    }
}

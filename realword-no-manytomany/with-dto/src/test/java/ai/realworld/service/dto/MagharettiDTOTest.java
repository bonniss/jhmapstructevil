package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MagharettiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MagharettiDTO.class);
        MagharettiDTO magharettiDTO1 = new MagharettiDTO();
        magharettiDTO1.setId(1L);
        MagharettiDTO magharettiDTO2 = new MagharettiDTO();
        assertThat(magharettiDTO1).isNotEqualTo(magharettiDTO2);
        magharettiDTO2.setId(magharettiDTO1.getId());
        assertThat(magharettiDTO1).isEqualTo(magharettiDTO2);
        magharettiDTO2.setId(2L);
        assertThat(magharettiDTO1).isNotEqualTo(magharettiDTO2);
        magharettiDTO1.setId(null);
        assertThat(magharettiDTO1).isNotEqualTo(magharettiDTO2);
    }
}

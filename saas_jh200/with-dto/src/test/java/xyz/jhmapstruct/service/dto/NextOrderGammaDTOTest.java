package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextOrderGammaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextOrderGammaDTO.class);
        NextOrderGammaDTO nextOrderGammaDTO1 = new NextOrderGammaDTO();
        nextOrderGammaDTO1.setId(1L);
        NextOrderGammaDTO nextOrderGammaDTO2 = new NextOrderGammaDTO();
        assertThat(nextOrderGammaDTO1).isNotEqualTo(nextOrderGammaDTO2);
        nextOrderGammaDTO2.setId(nextOrderGammaDTO1.getId());
        assertThat(nextOrderGammaDTO1).isEqualTo(nextOrderGammaDTO2);
        nextOrderGammaDTO2.setId(2L);
        assertThat(nextOrderGammaDTO1).isNotEqualTo(nextOrderGammaDTO2);
        nextOrderGammaDTO1.setId(null);
        assertThat(nextOrderGammaDTO1).isNotEqualTo(nextOrderGammaDTO2);
    }
}

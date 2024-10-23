package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextProductGammaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextProductGammaDTO.class);
        NextProductGammaDTO nextProductGammaDTO1 = new NextProductGammaDTO();
        nextProductGammaDTO1.setId(1L);
        NextProductGammaDTO nextProductGammaDTO2 = new NextProductGammaDTO();
        assertThat(nextProductGammaDTO1).isNotEqualTo(nextProductGammaDTO2);
        nextProductGammaDTO2.setId(nextProductGammaDTO1.getId());
        assertThat(nextProductGammaDTO1).isEqualTo(nextProductGammaDTO2);
        nextProductGammaDTO2.setId(2L);
        assertThat(nextProductGammaDTO1).isNotEqualTo(nextProductGammaDTO2);
        nextProductGammaDTO1.setId(null);
        assertThat(nextProductGammaDTO1).isNotEqualTo(nextProductGammaDTO2);
    }
}

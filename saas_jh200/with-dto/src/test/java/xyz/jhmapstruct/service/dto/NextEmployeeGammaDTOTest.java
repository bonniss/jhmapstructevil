package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextEmployeeGammaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextEmployeeGammaDTO.class);
        NextEmployeeGammaDTO nextEmployeeGammaDTO1 = new NextEmployeeGammaDTO();
        nextEmployeeGammaDTO1.setId(1L);
        NextEmployeeGammaDTO nextEmployeeGammaDTO2 = new NextEmployeeGammaDTO();
        assertThat(nextEmployeeGammaDTO1).isNotEqualTo(nextEmployeeGammaDTO2);
        nextEmployeeGammaDTO2.setId(nextEmployeeGammaDTO1.getId());
        assertThat(nextEmployeeGammaDTO1).isEqualTo(nextEmployeeGammaDTO2);
        nextEmployeeGammaDTO2.setId(2L);
        assertThat(nextEmployeeGammaDTO1).isNotEqualTo(nextEmployeeGammaDTO2);
        nextEmployeeGammaDTO1.setId(null);
        assertThat(nextEmployeeGammaDTO1).isNotEqualTo(nextEmployeeGammaDTO2);
    }
}

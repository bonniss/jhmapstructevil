package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextEmployeeBetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextEmployeeBetaDTO.class);
        NextEmployeeBetaDTO nextEmployeeBetaDTO1 = new NextEmployeeBetaDTO();
        nextEmployeeBetaDTO1.setId(1L);
        NextEmployeeBetaDTO nextEmployeeBetaDTO2 = new NextEmployeeBetaDTO();
        assertThat(nextEmployeeBetaDTO1).isNotEqualTo(nextEmployeeBetaDTO2);
        nextEmployeeBetaDTO2.setId(nextEmployeeBetaDTO1.getId());
        assertThat(nextEmployeeBetaDTO1).isEqualTo(nextEmployeeBetaDTO2);
        nextEmployeeBetaDTO2.setId(2L);
        assertThat(nextEmployeeBetaDTO1).isNotEqualTo(nextEmployeeBetaDTO2);
        nextEmployeeBetaDTO1.setId(null);
        assertThat(nextEmployeeBetaDTO1).isNotEqualTo(nextEmployeeBetaDTO2);
    }
}

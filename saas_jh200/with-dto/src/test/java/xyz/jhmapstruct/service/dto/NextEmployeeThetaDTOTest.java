package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextEmployeeThetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextEmployeeThetaDTO.class);
        NextEmployeeThetaDTO nextEmployeeThetaDTO1 = new NextEmployeeThetaDTO();
        nextEmployeeThetaDTO1.setId(1L);
        NextEmployeeThetaDTO nextEmployeeThetaDTO2 = new NextEmployeeThetaDTO();
        assertThat(nextEmployeeThetaDTO1).isNotEqualTo(nextEmployeeThetaDTO2);
        nextEmployeeThetaDTO2.setId(nextEmployeeThetaDTO1.getId());
        assertThat(nextEmployeeThetaDTO1).isEqualTo(nextEmployeeThetaDTO2);
        nextEmployeeThetaDTO2.setId(2L);
        assertThat(nextEmployeeThetaDTO1).isNotEqualTo(nextEmployeeThetaDTO2);
        nextEmployeeThetaDTO1.setId(null);
        assertThat(nextEmployeeThetaDTO1).isNotEqualTo(nextEmployeeThetaDTO2);
    }
}

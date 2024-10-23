package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextEmployeeSigmaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextEmployeeSigmaDTO.class);
        NextEmployeeSigmaDTO nextEmployeeSigmaDTO1 = new NextEmployeeSigmaDTO();
        nextEmployeeSigmaDTO1.setId(1L);
        NextEmployeeSigmaDTO nextEmployeeSigmaDTO2 = new NextEmployeeSigmaDTO();
        assertThat(nextEmployeeSigmaDTO1).isNotEqualTo(nextEmployeeSigmaDTO2);
        nextEmployeeSigmaDTO2.setId(nextEmployeeSigmaDTO1.getId());
        assertThat(nextEmployeeSigmaDTO1).isEqualTo(nextEmployeeSigmaDTO2);
        nextEmployeeSigmaDTO2.setId(2L);
        assertThat(nextEmployeeSigmaDTO1).isNotEqualTo(nextEmployeeSigmaDTO2);
        nextEmployeeSigmaDTO1.setId(null);
        assertThat(nextEmployeeSigmaDTO1).isNotEqualTo(nextEmployeeSigmaDTO2);
    }
}

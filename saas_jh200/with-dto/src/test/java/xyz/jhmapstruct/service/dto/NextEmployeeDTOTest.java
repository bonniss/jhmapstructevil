package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextEmployeeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextEmployeeDTO.class);
        NextEmployeeDTO nextEmployeeDTO1 = new NextEmployeeDTO();
        nextEmployeeDTO1.setId(1L);
        NextEmployeeDTO nextEmployeeDTO2 = new NextEmployeeDTO();
        assertThat(nextEmployeeDTO1).isNotEqualTo(nextEmployeeDTO2);
        nextEmployeeDTO2.setId(nextEmployeeDTO1.getId());
        assertThat(nextEmployeeDTO1).isEqualTo(nextEmployeeDTO2);
        nextEmployeeDTO2.setId(2L);
        assertThat(nextEmployeeDTO1).isNotEqualTo(nextEmployeeDTO2);
        nextEmployeeDTO1.setId(null);
        assertThat(nextEmployeeDTO1).isNotEqualTo(nextEmployeeDTO2);
    }
}

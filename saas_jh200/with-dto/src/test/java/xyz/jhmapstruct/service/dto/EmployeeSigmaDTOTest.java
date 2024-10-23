package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class EmployeeSigmaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeSigmaDTO.class);
        EmployeeSigmaDTO employeeSigmaDTO1 = new EmployeeSigmaDTO();
        employeeSigmaDTO1.setId(1L);
        EmployeeSigmaDTO employeeSigmaDTO2 = new EmployeeSigmaDTO();
        assertThat(employeeSigmaDTO1).isNotEqualTo(employeeSigmaDTO2);
        employeeSigmaDTO2.setId(employeeSigmaDTO1.getId());
        assertThat(employeeSigmaDTO1).isEqualTo(employeeSigmaDTO2);
        employeeSigmaDTO2.setId(2L);
        assertThat(employeeSigmaDTO1).isNotEqualTo(employeeSigmaDTO2);
        employeeSigmaDTO1.setId(null);
        assertThat(employeeSigmaDTO1).isNotEqualTo(employeeSigmaDTO2);
    }
}

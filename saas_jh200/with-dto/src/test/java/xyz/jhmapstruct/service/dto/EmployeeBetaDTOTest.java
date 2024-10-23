package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class EmployeeBetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeBetaDTO.class);
        EmployeeBetaDTO employeeBetaDTO1 = new EmployeeBetaDTO();
        employeeBetaDTO1.setId(1L);
        EmployeeBetaDTO employeeBetaDTO2 = new EmployeeBetaDTO();
        assertThat(employeeBetaDTO1).isNotEqualTo(employeeBetaDTO2);
        employeeBetaDTO2.setId(employeeBetaDTO1.getId());
        assertThat(employeeBetaDTO1).isEqualTo(employeeBetaDTO2);
        employeeBetaDTO2.setId(2L);
        assertThat(employeeBetaDTO1).isNotEqualTo(employeeBetaDTO2);
        employeeBetaDTO1.setId(null);
        assertThat(employeeBetaDTO1).isNotEqualTo(employeeBetaDTO2);
    }
}

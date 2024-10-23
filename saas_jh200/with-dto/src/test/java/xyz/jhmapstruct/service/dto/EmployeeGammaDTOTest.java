package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class EmployeeGammaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeGammaDTO.class);
        EmployeeGammaDTO employeeGammaDTO1 = new EmployeeGammaDTO();
        employeeGammaDTO1.setId(1L);
        EmployeeGammaDTO employeeGammaDTO2 = new EmployeeGammaDTO();
        assertThat(employeeGammaDTO1).isNotEqualTo(employeeGammaDTO2);
        employeeGammaDTO2.setId(employeeGammaDTO1.getId());
        assertThat(employeeGammaDTO1).isEqualTo(employeeGammaDTO2);
        employeeGammaDTO2.setId(2L);
        assertThat(employeeGammaDTO1).isNotEqualTo(employeeGammaDTO2);
        employeeGammaDTO1.setId(null);
        assertThat(employeeGammaDTO1).isNotEqualTo(employeeGammaDTO2);
    }
}

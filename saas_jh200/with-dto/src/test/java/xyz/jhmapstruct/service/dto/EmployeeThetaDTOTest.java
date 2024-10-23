package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class EmployeeThetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeThetaDTO.class);
        EmployeeThetaDTO employeeThetaDTO1 = new EmployeeThetaDTO();
        employeeThetaDTO1.setId(1L);
        EmployeeThetaDTO employeeThetaDTO2 = new EmployeeThetaDTO();
        assertThat(employeeThetaDTO1).isNotEqualTo(employeeThetaDTO2);
        employeeThetaDTO2.setId(employeeThetaDTO1.getId());
        assertThat(employeeThetaDTO1).isEqualTo(employeeThetaDTO2);
        employeeThetaDTO2.setId(2L);
        assertThat(employeeThetaDTO1).isNotEqualTo(employeeThetaDTO2);
        employeeThetaDTO1.setId(null);
        assertThat(employeeThetaDTO1).isNotEqualTo(employeeThetaDTO2);
    }
}

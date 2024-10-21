package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class EmployeeMiMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeMiMiDTO.class);
        EmployeeMiMiDTO employeeMiMiDTO1 = new EmployeeMiMiDTO();
        employeeMiMiDTO1.setId(1L);
        EmployeeMiMiDTO employeeMiMiDTO2 = new EmployeeMiMiDTO();
        assertThat(employeeMiMiDTO1).isNotEqualTo(employeeMiMiDTO2);
        employeeMiMiDTO2.setId(employeeMiMiDTO1.getId());
        assertThat(employeeMiMiDTO1).isEqualTo(employeeMiMiDTO2);
        employeeMiMiDTO2.setId(2L);
        assertThat(employeeMiMiDTO1).isNotEqualTo(employeeMiMiDTO2);
        employeeMiMiDTO1.setId(null);
        assertThat(employeeMiMiDTO1).isNotEqualTo(employeeMiMiDTO2);
    }
}

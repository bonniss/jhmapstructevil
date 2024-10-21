package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class EmployeeMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeMiDTO.class);
        EmployeeMiDTO employeeMiDTO1 = new EmployeeMiDTO();
        employeeMiDTO1.setId(1L);
        EmployeeMiDTO employeeMiDTO2 = new EmployeeMiDTO();
        assertThat(employeeMiDTO1).isNotEqualTo(employeeMiDTO2);
        employeeMiDTO2.setId(employeeMiDTO1.getId());
        assertThat(employeeMiDTO1).isEqualTo(employeeMiDTO2);
        employeeMiDTO2.setId(2L);
        assertThat(employeeMiDTO1).isNotEqualTo(employeeMiDTO2);
        employeeMiDTO1.setId(null);
        assertThat(employeeMiDTO1).isNotEqualTo(employeeMiDTO2);
    }
}

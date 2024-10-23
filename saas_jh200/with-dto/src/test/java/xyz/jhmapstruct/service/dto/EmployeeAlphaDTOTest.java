package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class EmployeeAlphaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeAlphaDTO.class);
        EmployeeAlphaDTO employeeAlphaDTO1 = new EmployeeAlphaDTO();
        employeeAlphaDTO1.setId(1L);
        EmployeeAlphaDTO employeeAlphaDTO2 = new EmployeeAlphaDTO();
        assertThat(employeeAlphaDTO1).isNotEqualTo(employeeAlphaDTO2);
        employeeAlphaDTO2.setId(employeeAlphaDTO1.getId());
        assertThat(employeeAlphaDTO1).isEqualTo(employeeAlphaDTO2);
        employeeAlphaDTO2.setId(2L);
        assertThat(employeeAlphaDTO1).isNotEqualTo(employeeAlphaDTO2);
        employeeAlphaDTO1.setId(null);
        assertThat(employeeAlphaDTO1).isNotEqualTo(employeeAlphaDTO2);
    }
}

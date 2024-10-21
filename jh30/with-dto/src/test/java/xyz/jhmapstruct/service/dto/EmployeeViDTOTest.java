package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class EmployeeViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeViDTO.class);
        EmployeeViDTO employeeViDTO1 = new EmployeeViDTO();
        employeeViDTO1.setId(1L);
        EmployeeViDTO employeeViDTO2 = new EmployeeViDTO();
        assertThat(employeeViDTO1).isNotEqualTo(employeeViDTO2);
        employeeViDTO2.setId(employeeViDTO1.getId());
        assertThat(employeeViDTO1).isEqualTo(employeeViDTO2);
        employeeViDTO2.setId(2L);
        assertThat(employeeViDTO1).isNotEqualTo(employeeViDTO2);
        employeeViDTO1.setId(null);
        assertThat(employeeViDTO1).isNotEqualTo(employeeViDTO2);
    }
}

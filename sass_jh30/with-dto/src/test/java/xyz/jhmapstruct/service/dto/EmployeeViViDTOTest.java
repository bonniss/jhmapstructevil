package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class EmployeeViViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeViViDTO.class);
        EmployeeViViDTO employeeViViDTO1 = new EmployeeViViDTO();
        employeeViViDTO1.setId(1L);
        EmployeeViViDTO employeeViViDTO2 = new EmployeeViViDTO();
        assertThat(employeeViViDTO1).isNotEqualTo(employeeViViDTO2);
        employeeViViDTO2.setId(employeeViViDTO1.getId());
        assertThat(employeeViViDTO1).isEqualTo(employeeViViDTO2);
        employeeViViDTO2.setId(2L);
        assertThat(employeeViViDTO1).isNotEqualTo(employeeViViDTO2);
        employeeViViDTO1.setId(null);
        assertThat(employeeViViDTO1).isNotEqualTo(employeeViViDTO2);
    }
}

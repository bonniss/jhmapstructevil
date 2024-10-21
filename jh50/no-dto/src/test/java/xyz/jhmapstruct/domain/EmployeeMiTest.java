package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.EmployeeMiTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class EmployeeMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeMi.class);
        EmployeeMi employeeMi1 = getEmployeeMiSample1();
        EmployeeMi employeeMi2 = new EmployeeMi();
        assertThat(employeeMi1).isNotEqualTo(employeeMi2);

        employeeMi2.setId(employeeMi1.getId());
        assertThat(employeeMi1).isEqualTo(employeeMi2);

        employeeMi2 = getEmployeeMiSample2();
        assertThat(employeeMi1).isNotEqualTo(employeeMi2);
    }
}

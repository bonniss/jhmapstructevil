package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.EmployeeThetaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class EmployeeThetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeTheta.class);
        EmployeeTheta employeeTheta1 = getEmployeeThetaSample1();
        EmployeeTheta employeeTheta2 = new EmployeeTheta();
        assertThat(employeeTheta1).isNotEqualTo(employeeTheta2);

        employeeTheta2.setId(employeeTheta1.getId());
        assertThat(employeeTheta1).isEqualTo(employeeTheta2);

        employeeTheta2 = getEmployeeThetaSample2();
        assertThat(employeeTheta1).isNotEqualTo(employeeTheta2);
    }

    @Test
    void tenantTest() {
        EmployeeTheta employeeTheta = getEmployeeThetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        employeeTheta.setTenant(masterTenantBack);
        assertThat(employeeTheta.getTenant()).isEqualTo(masterTenantBack);

        employeeTheta.tenant(null);
        assertThat(employeeTheta.getTenant()).isNull();
    }
}

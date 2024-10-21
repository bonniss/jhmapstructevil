package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.EmployeeViTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class EmployeeViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeVi.class);
        EmployeeVi employeeVi1 = getEmployeeViSample1();
        EmployeeVi employeeVi2 = new EmployeeVi();
        assertThat(employeeVi1).isNotEqualTo(employeeVi2);

        employeeVi2.setId(employeeVi1.getId());
        assertThat(employeeVi1).isEqualTo(employeeVi2);

        employeeVi2 = getEmployeeViSample2();
        assertThat(employeeVi1).isNotEqualTo(employeeVi2);
    }

    @Test
    void tenantTest() {
        EmployeeVi employeeVi = getEmployeeViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        employeeVi.setTenant(masterTenantBack);
        assertThat(employeeVi.getTenant()).isEqualTo(masterTenantBack);

        employeeVi.tenant(null);
        assertThat(employeeVi.getTenant()).isNull();
    }
}

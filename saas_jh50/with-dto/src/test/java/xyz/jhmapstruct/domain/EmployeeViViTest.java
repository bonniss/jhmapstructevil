package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.EmployeeViViTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class EmployeeViViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeViVi.class);
        EmployeeViVi employeeViVi1 = getEmployeeViViSample1();
        EmployeeViVi employeeViVi2 = new EmployeeViVi();
        assertThat(employeeViVi1).isNotEqualTo(employeeViVi2);

        employeeViVi2.setId(employeeViVi1.getId());
        assertThat(employeeViVi1).isEqualTo(employeeViVi2);

        employeeViVi2 = getEmployeeViViSample2();
        assertThat(employeeViVi1).isNotEqualTo(employeeViVi2);
    }

    @Test
    void tenantTest() {
        EmployeeViVi employeeViVi = getEmployeeViViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        employeeViVi.setTenant(masterTenantBack);
        assertThat(employeeViVi.getTenant()).isEqualTo(masterTenantBack);

        employeeViVi.tenant(null);
        assertThat(employeeViVi.getTenant()).isNull();
    }
}

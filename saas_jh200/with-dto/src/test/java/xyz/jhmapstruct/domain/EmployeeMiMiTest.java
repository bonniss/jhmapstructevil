package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.EmployeeMiMiTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class EmployeeMiMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeMiMi.class);
        EmployeeMiMi employeeMiMi1 = getEmployeeMiMiSample1();
        EmployeeMiMi employeeMiMi2 = new EmployeeMiMi();
        assertThat(employeeMiMi1).isNotEqualTo(employeeMiMi2);

        employeeMiMi2.setId(employeeMiMi1.getId());
        assertThat(employeeMiMi1).isEqualTo(employeeMiMi2);

        employeeMiMi2 = getEmployeeMiMiSample2();
        assertThat(employeeMiMi1).isNotEqualTo(employeeMiMi2);
    }

    @Test
    void tenantTest() {
        EmployeeMiMi employeeMiMi = getEmployeeMiMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        employeeMiMi.setTenant(masterTenantBack);
        assertThat(employeeMiMi.getTenant()).isEqualTo(masterTenantBack);

        employeeMiMi.tenant(null);
        assertThat(employeeMiMi.getTenant()).isNull();
    }
}

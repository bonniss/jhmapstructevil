package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.EmployeeAlphaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class EmployeeAlphaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeAlpha.class);
        EmployeeAlpha employeeAlpha1 = getEmployeeAlphaSample1();
        EmployeeAlpha employeeAlpha2 = new EmployeeAlpha();
        assertThat(employeeAlpha1).isNotEqualTo(employeeAlpha2);

        employeeAlpha2.setId(employeeAlpha1.getId());
        assertThat(employeeAlpha1).isEqualTo(employeeAlpha2);

        employeeAlpha2 = getEmployeeAlphaSample2();
        assertThat(employeeAlpha1).isNotEqualTo(employeeAlpha2);
    }

    @Test
    void tenantTest() {
        EmployeeAlpha employeeAlpha = getEmployeeAlphaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        employeeAlpha.setTenant(masterTenantBack);
        assertThat(employeeAlpha.getTenant()).isEqualTo(masterTenantBack);

        employeeAlpha.tenant(null);
        assertThat(employeeAlpha.getTenant()).isNull();
    }
}
